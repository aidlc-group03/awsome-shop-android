# Android App — 业务逻辑模型

## 页面导航结构

```mermaid
flowchart TD
    subgraph Auth["认证"]
        Login["LoginScreen<br/>登录"]
    end
    
    subgraph Main["主页面 (BottomNav)"]
        Home["HomeScreen<br/>商城首页"]
        Orders["OrdersScreen<br/>兑换记录"]
        Points["PointsCenterScreen<br/>积分中心"]
    end
    
    subgraph Detail["详情页"]
        ProdDetail["ProductDetailScreen<br/>商品详情"]
        OrderDetail["OrderDetailScreen<br/>订单详情"]
        PointsHistory["PointsHistoryScreen<br/>积分记录"]
    end
    
    subgraph Redeem["兑换流程"]
        DeliveryInfo["DeliveryInfoScreen<br/>配送信息"]
        ConfirmRedeem["ConfirmRedemptionScreen<br/>确认兑换"]
        RedeemSuccess["RedemptionSuccessScreen<br/>兑换成功"]
    end
    
    Login -->|成功| Home
    Home --> ProdDetail
    ProdDetail --> DeliveryInfo
    DeliveryInfo --> ConfirmRedeem
    ConfirmRedeem --> RedeemSuccess
    Orders --> OrderDetail
    Points --> PointsHistory
```

---

## 核心业务流程

### 1. 登录流程

```mermaid
flowchart TD
    Start(["App启动"])
    CheckToken{本地有Token?}
    ValidToken{Token有效?}
    ShowLogin["显示登录页"]
    Input["输入用户名+密码"]
    CallAPI["POST auth/login"]
    Success{成功?}
    SaveToken["存储Token<br/>存储UserInfo"]
    GoHome["导航到首页"]
    ShowError["显示错误Toast"]
    
    Start --> CheckToken
    CheckToken -->|否| ShowLogin
    CheckToken -->|是| ValidToken
    ValidToken -->|是| GoHome
    ValidToken -->|否| ShowLogin
    ShowLogin --> Input
    Input --> CallAPI
    CallAPI --> Success
    Success -->|是| SaveToken
    SaveToken --> GoHome
    Success -->|否| ShowError
    ShowError --> ShowLogin
```

### 2. 商品浏览与兑换流程

```mermaid
flowchart TD
    Home["首页<br/>商品列表 + 分类"]
    SelectCat["选择分类"]
    ClickProd["点击商品"]
    Detail["商品详情<br/>图片/名称/积分/描述/规格"]
    CheckStock{有库存?}
    CheckPoints{积分充足?}
    ClickRedeem["点击兑换"]
    Delivery["配送信息页<br/>收件人/手机/地址"]
    Validate{验证通过?}
    Confirm["确认兑换页<br/>商品摘要+配送+积分"]
    CallOrder["POST orders"]
    OrderResult{成功?}
    SuccessPage["兑换成功页"]
    ErrorToast["Toast错误提示"]
    
    Home --> SelectCat
    SelectCat --> Home
    Home --> ClickProd
    ClickProd --> Detail
    Detail --> CheckStock
    CheckStock -->|否| Detail
    CheckStock -->|是| CheckPoints
    CheckPoints -->|否| Detail
    CheckPoints -->|是| ClickRedeem
    ClickRedeem --> Delivery
    Delivery --> Validate
    Validate -->|否| Delivery
    Validate -->|通过| Confirm
    Confirm --> CallOrder
    CallOrder --> OrderResult
    OrderResult -->|成功| SuccessPage
    OrderResult -->|失败| ErrorToast
```

---

## 屏幕组件设计

| Screen | 主要组件 | API调用 |
|--------|---------|---------|
| LoginScreen | TextField(username), TextField(password), Button(login) | POST auth/login |
| HomeScreen | CategoryChips, LazyColumn(ProductCard), PullRefresh | GET products, GET categories |
| ProductDetailScreen | AsyncImage, Text(details), SpecChips, RedeemButton | GET products/{id} |
| DeliveryInfoScreen | TextField(name), TextField(phone), TextField(address) | 无 (本地state) |
| ConfirmRedemptionScreen | ProductSummary, DeliveryInfo, PointsCost, ConfirmBtn | POST orders |
| RedemptionSuccessScreen | SuccessIcon, OrderNo, ProductInfo | 无 (路由参数) |
| OrdersScreen | LazyColumn(OrderCard), StatusFilter | GET orders |
| OrderDetailScreen | OrderInfo, StatusTimeline, DeliveryInfo | GET orders/{id} |
| PointsCenterScreen | BalanceCard, StatsRow | GET points/balance |
| PointsHistoryScreen | LazyColumn(TransactionItem), TypeFilter | GET points/transactions |

---

## API 对接改造

### ApiService.kt 需要调整的接口路径

| 当前路径 | 调整后路径 | 说明 |
|---------|-----------|------|
| `auth/login` | `/api/v1/public/auth/login` | 补全完整路径 |
| `user/profile` | `/api/v1/user/profile` | 补全前缀 |
| `products` | `/api/v1/public/product/list` | POST改为GET或调整 |
| `products/{id}` | `/api/v1/public/product/{id}` | 补全前缀 |
| `orders` (POST) | `/api/v1/order/create` | 调整路径 |
| `orders` (GET) | `/api/v1/order/list` | 调整为POST分页 |
| `orders/{id}` | `/api/v1/order/{id}` | 补全前缀 |
| `points/transactions` | `/api/v1/points/transactions` | 补全前缀 |

### 认证Token管理
```kotlin
// OkHttp Interceptor 自动附加 Bearer Token
class AuthInterceptor @Inject constructor(
    private val tokenStore: TokenStore
) : Interceptor {
    override fun intercept(chain: Chain): Response {
        val token = tokenStore.getToken()
        val request = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else chain.request()
        
        val response = chain.proceed(request)
        if (response.code == 401) {
            tokenStore.clear()
            // 触发跳转登录页
        }
        return response
    }
}
```

### 响应包装处理
```kotlin
// 统一处理后端 Result<T> 响应格式
data class ApiResult<T>(
    val code: Int,
    val message: String,
    val data: T?
)

// Repository 层统一解包
suspend fun <T> safeApiCall(call: suspend () -> ApiResult<T>): Result<T> {
    return try {
        val response = call()
        if (response.code == 0 && response.data != null) {
            Result.success(response.data)
        } else {
            Result.failure(ApiException(response.code, response.message))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

---

## 数据模型调整

### Product (对齐后端)
```kotlin
@Serializable
data class Product(
    val id: Long,
    val name: String,
    val sku: String,
    val category: String,
    val brand: String? = null,
    val pointsPrice: Int,
    val marketPrice: Double? = null,
    val stock: Int,
    val soldCount: Int,
    val status: Int,
    val description: String? = null,
    val imageUrl: String? = null,
    val subtitle: String? = null,
    val deliveryMethod: String? = null,
    val serviceGuarantee: String? = null,
    val promotion: String? = null,
    val colors: String? = null,
    val specs: List<SpecItem>? = null,
)

@Serializable
data class SpecItem(val key: String, val value: String)
```

### Order (对齐后端)
```kotlin
@Serializable
data class Order(
    val id: Long,
    val orderNo: String,
    val productId: Long,
    val productName: String,
    val pointsAmount: Int,
    val quantity: Int,
    val status: String, // pending/confirmed/shipping/completed/cancelled
    val recipientName: String,
    val recipientPhone: String,
    val recipientAddress: String,
    val createdAt: String,
)
```

### User (对齐后端)
```kotlin
@Serializable
data class User(
    val id: Long,
    val username: String,
    val displayName: String,
    val email: String? = null,
    val role: String,
    val avatarUrl: String? = null,
)
```

### PointsBalance (新增)
```kotlin
@Serializable
data class PointsBalance(
    val balance: Int,
    val totalEarned: Int,
    val totalSpent: Int,
)
```

### PointsTransaction (对齐后端)
```kotlin
@Serializable
data class PointsTransaction(
    val id: Long,
    val type: String, // earn/spend/refund/admin_add/admin_deduct
    val points: Int,
    val balanceAfter: Int,
    val description: String? = null,
    val createdAt: String,
)
```

### CreateOrderRequest (对齐后端)
```kotlin
@Serializable
data class CreateOrderRequest(
    val productId: Long,
    val recipientName: String,
    val recipientPhone: String,
    val recipientAddress: String,
)
```

---

## 本地存储

| Key | 类型 | 用途 |
|-----|------|------|
| auth_token | String | JWT Token |
| user_info | JSON | 用户基本信息 (UserInfo) |

### TokenStore
```kotlin
@Singleton
class TokenStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    
    fun getToken(): String? = prefs.getString("auth_token", null)
    fun saveToken(token: String) = prefs.edit().putString("auth_token", token).apply()
    fun clear() = prefs.edit().clear().apply()
}
```
