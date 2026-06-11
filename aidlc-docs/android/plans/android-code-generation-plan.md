# Unit 7: Android App — 代码生成计划

> **本计划是 Unit 7 代码生成阶段的唯一事实来源 (Single Source of Truth)。**
> 所有代码生成严格按照本计划的步骤顺序执行，完成一步勾选一步。

## 单元上下文

| 属性 | 内容 |
|------|------|
| **单元** | Unit 7: Android App |
| **模块路径** | `awsome-shop-android/` (Workspace 根目录下，**绝不**写入 aidlc-docs/) |
| **类型** | Brownfield — 在现有 Kotlin + Jetpack Compose + Hilt + Retrofit 骨架上扩展 |
| **技术栈** | Kotlin 2.1 / Compose BOM 2024.12 / Material3 / Navigation-Compose 2.8 (type-safe) / Hilt 2.53 / Retrofit 2.11 + kotlinx-serialization / OkHttp 4.12 / Coil 2.7 / DataStore 1.1 / Coroutines 1.9；minSdk 26 / targetSdk 35 / JVM 17 |
| **依赖** | 所有后端服务 (通过 Gateway `http://10.0.2.2:8080/api/`)；**后端 API 尚未联调 → 接口契约来自 functional-design 与 api-documentation.md**，构建以编译通过为验收 |
| **代码仓库** | **`awsome-shop-android/`** (Workspace 根目录下的独立 git repo，含自己的 `.git/`)；应用代码写入此 repo，文档汇总写入 `aidlc-docs/construction/android/code/` |
| **设计稿** | `awsome-shop-plan/doc/awsome-shop.pen` (通过 Pencil MCP 读取 **390px Android 帧**) |
| **实现流程** | 遵循 **`ui-implement` skill**：每屏 = Pencil MCP 读 Android 设计帧 → 生成 Composable → 编译验证 (与 Unit 6 复用同一 skill) |
| **对应故事** | US-E01~E11 (员工端故事) |

## 功能设计来源

- `awsome-shop-android/aidlc-docs/android/functional-design/domain-entities.md` (数据模型、ViewModel、Repository、ApiService、导航)
- `awsome-shop-android/aidlc-docs/android/functional-design/business-logic-model.md` (页面导航、业务流程、API 对接改造、本地存储)
- `awsome-shop-android/aidlc-docs/android/functional-design/business-rules.md` (BR-A1~BR-A7 业务规则)
- 镜像副本：`aidlc-docs/construction/android/functional-design/` (内容一致)

## 设计稿映射 (Pencil 帧 → 屏幕)

| Pencil 帧 ID | 帧名称 (390px) | 目标 Screen | 状态 |
|------|------|------|------|
| lv1FB | Android - Login | `ui/screens/login/LoginScreen` | 改造 |
| cx9I1 | Android - Shop Home | `ui/screens/home/HomeScreen` | 改造 |
| M53eH5 | Android - Product Detail | `ui/screens/product/ProductDetailScreen` | 改造 |
| LHbg9 | Android - Confirm Redemption | `ui/screens/redemption/ConfirmRedemptionScreen` | 改造 |
| xmpNW | Android - Delivery Info | `ui/screens/redemption/DeliveryInfoScreen` | 改造 |
| NyxHt | Android - Redemption Success | `ui/screens/redemption/RedemptionSuccessScreen` | 改造 |
| V70qWL | Android - Redemption History | `ui/screens/order/OrdersScreen` | 改造 |
| G0qt7 | Android - Order Detail | `ui/screens/order/OrderDetailScreen` | 改造 |
| BffPX | Android - Points Center | `ui/screens/points/PointsCenterScreen` | 改造 |
| kHRuf | Android - Points History | `ui/screens/points/PointsHistoryScreen` | 改造 |
| ZaNvp | Android - My Account | `ui/screens/profile/ProfileScreen` | 改造 |
| GbRV7 | Android - Logout Dialog | `ProfileScreen` 内对话框 | 改造 |

---

## 关键架构决策 (功能设计 vs 现有代码的取舍)

> 现有项目已搭好骨架（screens/repositories/ApiService/导航），本单元为 **联调改造**。以下为遇到的冲突点及处理原则（遵循 brownfield「保留可用、最小修改」）：

1. **导航路由**：现有代码使用 **type-safe Navigation** (`Route` sealed class + `composable<Route.X>`)，功能设计文档示意的是 string `Routes` object。
   → **保留现有 type-safe `Route` 方案**，仅补充缺失参数 (如 `RedemptionSuccess(orderNo)`)。不引入 string Routes。
2. **Token 存储**：现有 `AuthRepository` 用 DataStore，但 `getToken()` 使用 `dataStore.data.collect{}` 永不返回（隐藏 bug），且 OkHttp Interceptor 需 **同步** 读取 token。
   → 按功能设计新增 **`TokenStore` (SharedPreferences，同步读写)**，供 `AuthInterceptor` 使用；重构 `AuthRepository` 改用 `TokenStore`，移除 DataStore 误用。
3. **数据模型**：现有模型 (Product/Order 等) 字段与后端不一致 (如 `points` vs `pointsPrice`、`id: String` vs `Long`)。
   → 按功能设计 **对齐后端字段**，统一 `id: Long`、新增 `PointsBalance`/`Category`/`ApiResult`/`PageResult`。
4. **ViewModel 层**：现有 screens 直接持有回调、使用 mock，无 ViewModel。
   → 按功能设计 **新增 7 个 ViewModel**，screens 通过 `hiltViewModel()` 注入，`collectAsStateWithLifecycle()` 观察。
5. **响应包装**：后端统一返回 `ApiResult<T>{code,message,data}`。
   → ApiService 返回 `ApiResult<T>`，Repository 用 `safeApiCall` 解包为 `Result<T>`。
6. **NFR**：本单元 NFR 阶段已 SKIP (MVP 功能优先)，错误处理按 BR-A6 实现，不做额外性能/安全加固。

---

# PART 1: 规划 (本文档)

## Step 1: 分析单元上下文 ✅
- [x] 阅读功能设计制品 (domain-entities / business-logic-model / business-rules)
- [x] 阅读现有 Android 代码结构 (App / MainActivity / data 层 / di / 导航 / screens / theme)
- [x] 阅读 Pencil 设计稿 12 个 Android 帧，建立帧→屏幕映射
- [x] 确认 API 契约 (functional-design ApiService 定义 + 后端 `/api/v1/**` 路径)
- [x] 识别架构冲突点并确定处理原则 (见上「关键架构决策」)

## Step 2: 确定代码位置 ✅
- [x] Workspace 根：`/Users/zhengjl/Developer/git-repo/AIDLC-Beijing-TTT/awsome-shop-project`
- [x] 应用代码：`awsome-shop-android/app/src/main/java/com/awsome/shop/**`
- [x] 文档汇总：`aidlc-docs/construction/android/code/*.md`

## Step 3: 数据模型层 (data/model)
> 对齐后端字段，新增缺失模型。BR 来源：domain-entities.md / business-logic-model.md
- [ ] 3.1 改造 `data/model/Product.kt` — 字段对齐 (`id:Long, name, sku, category, brand?, pointsPrice, marketPrice?, stock, soldCount, status, description?, imageUrl?, subtitle?, deliveryMethod?, serviceGuarantee?, promotion?, colors?, specs:List<SpecItem>?`)，新增 `SpecItem`
- [ ] 3.2 改造 `data/model/Order.kt` — 字段对齐 (`id:Long, orderNo, productId, productName, pointsAmount, quantity, status:String, recipientName, recipientPhone, recipientAddress, createdAt`)；保留状态展示映射工具
- [ ] 3.3 改造 `data/model/User.kt` — (`id:Long, username, displayName, email?, role, avatarUrl?`)
- [ ] 3.4 改造 `data/model/PointsTransaction.kt` — (`id:Long, type, points, balanceAfter, description?, createdAt`)
- [ ] 3.5 新增 `data/model/PointsBalance.kt` — (`balance, totalEarned, totalSpent`)
- [ ] 3.6 新增 `data/model/Category.kt` — (`id, name, ...`)
- [ ] 3.7 新增 `data/model/ApiResult.kt` — 统一响应包装 (`code, message, data:T?`)
- [ ] 3.8 新增 `data/model/PageResult.kt` — 分页结果 (`list, total, page, size`)
- [ ] 3.9 新增 `data/model/CreateOrderRequest.kt` + `ListProductRequest` / `ListOrderRequest` 请求体
- [ ] 3.10 评估并清理 `data/model/Address.kt` (功能设计无独立地址簿，配送信息为本地 state；按需移除或保留)
- [ ] **3.11 数据模型变更汇总** → `aidlc-docs/construction/android/code/01-data-models.md`

## Step 4: 网络与本地存储层 (data/remote, data/local)
> 对齐后端 `/api/v1/**` 路径，补全 Token 管理。BR 来源：BR-A1, BR-A6
- [ ] 4.1 改造 `data/remote/ApiService.kt` — 重写全部接口路径与签名，返回 `ApiResult<T>` (login / getProfile / getProducts / getProductDetail / getCategories / createOrder / getOrders / getOrderDetail / getPointsBalance / getPointsTransactions)
- [ ] 4.2 新增 `data/local/TokenStore.kt` — SharedPreferences 同步读写 token + userInfo (BR-A1.3)
- [ ] 4.3 新增 `data/remote/AuthInterceptor.kt` — 自动附加 `Bearer Token`；401 清除本地状态 (BR-A1.2, BR-A1.4)
- [ ] 4.4 新增 `data/remote/SafeApiCall.kt` — `safeApiCall` 解包 + `ApiException` (BR-A6.4)
- [ ] **4.5 网络层变更汇总** → `aidlc-docs/construction/android/code/02-network-layer.md`

## Step 5: 仓库层 (data/repository)
> BR 来源：BR-A1~BR-A5
- [ ] 5.1 改造 `data/repository/AuthRepository.kt` — 改用 `TokenStore`；`login()` 存 token+user；`isLoggedIn()` / `logout()` / `getProfile()`
- [ ] 5.2 改造 `data/repository/ShopRepository.kt` — 方法签名支持分页与 `ApiResult` (getProducts/getProductDetail/getCategories/createOrder/getOrders/getOrderDetail/getPointsBalance/getPointsTransactions)
- [ ] **5.3 仓库层变更汇总** → `aidlc-docs/construction/android/code/03-repositories.md`

## Step 6: 依赖注入 (di)
- [ ] 6.1 改造 `di/AppModule.kt` — 注入 `TokenStore`、`AuthInterceptor` (加入 OkHttp client)，确认 baseUrl 使用 `BuildConfig.BASE_URL`
- [ ] **6.2 DI 变更汇总** → `aidlc-docs/construction/android/code/04-di.md`

## Step 7: ViewModel 层 (ui/viewmodel) — 新增
> BR 来源：BR-A7 (sealed UiState / StateFlow / Hilt 注入)
- [ ] 7.1 新增 `ui/viewmodel/LoginViewModel.kt` (BR-A1)
- [ ] 7.2 新增 `ui/viewmodel/HomeViewModel.kt` (BR-A2：商品列表/分类/下拉刷新)
- [ ] 7.3 新增 `ui/viewmodel/ProductDetailViewModel.kt` (BR-A3.1：积分/库存校验)
- [ ] 7.4 新增 `ui/viewmodel/RedemptionViewModel.kt` (BR-A3：创建订单/防重复提交)
- [ ] 7.5 新增 `ui/viewmodel/OrdersViewModel.kt` (BR-A4：列表/状态筛选)
- [ ] 7.6 新增 `ui/viewmodel/PointsViewModel.kt` (BR-A5：余额/交易记录/类型筛选)
- [ ] 7.7 (按需) `ui/viewmodel/ProfileViewModel.kt` — 用户资料 + 登出
- [ ] **7.8 ViewModel 层汇总** → `aidlc-docs/construction/android/code/05-viewmodels.md`

## Step 8: 导航与全局状态 (ui/navigation, MainActivity)
> BR 来源：BR-A1.1 (启动鉴权), BR-A7.5 (参数传递)
- [ ] 8.1 改造 `ui/navigation/Routes.kt` (`Route` sealed class) — 补全参数 (`RedemptionSuccess(orderNo)`, `OrderDetail(orderId:Long)` 等)
- [ ] 8.2 改造 `ui/navigation/AppNavGraph.kt` — 接入 ViewModel 驱动，401 跳转登录
- [ ] 8.3 改造 `MainActivity.kt` / `AwsomeShopRoot.kt` — 启动检查 token 决定起始路由 (BR-A1.1)
- [ ] **8.4 导航变更汇总** → `aidlc-docs/construction/android/code/06-navigation.md`

## Step 9: UI 屏幕改造 (ui/screens) — 按 Pencil 设计帧逐屏实现
> 每屏遵循 `ui-implement` skill：Pencil MCP 读对应 390px 帧 → Composable 对齐视觉 → ViewModel 接入 → 编译验证。BR 来源：BR-A1~A7
- [ ] 9.1 `login/LoginScreen.kt` ← 帧 `lv1FB` (品牌头 + 登录卡片，接入 LoginViewModel)
- [ ] 9.2 `home/HomeScreen.kt` ← 帧 `cx9I1` (Hero/分类Chip/商品网格/底部导航 + PullRefresh，HomeViewModel)
- [ ] 9.3 `product/ProductDetailScreen.kt` ← 帧 `M53eH5` (图片/信息/规格 + 底部兑换按钮，ProductDetailViewModel)
- [ ] 9.4 `redemption/DeliveryInfoScreen.kt` ← 帧 `xmpNW` (收件人/手机11位/地址校验，BR-A3.2)
- [ ] 9.5 `redemption/ConfirmRedemptionScreen.kt` ← 帧 `LHbg9` (商品摘要/配送/积分 + Loading 防重复，RedemptionViewModel)
- [ ] 9.6 `redemption/RedemptionSuccessScreen.kt` ← 帧 `NyxHt` (成功图标/订单号/跳转详情)
- [ ] 9.7 `order/OrdersScreen.kt` ← 帧 `V70qWL` (状态 Tab 筛选/订单卡片/空态，OrdersViewModel)
- [ ] 9.8 `order/OrderDetailScreen.kt` ← 帧 `G0qt7` (状态/配送/明细)
- [ ] 9.9 `points/PointsCenterScreen.kt` ← 帧 `BffPX` (余额大字/统计/入口，PointsViewModel)
- [ ] 9.10 `points/PointsHistoryScreen.kt` ← 帧 `kHRuf` (类型筛选/收支颜色, BR-A5.5)
- [ ] 9.11 `profile/ProfileScreen.kt` ← 帧 `ZaNvp` + 登出对话框 `GbRV7` (BR-A1.5)
- [ ] 9.12 复用/改造 `ui/components` (BottomNavBar, CategoryChip) + 新增通用组件 (LoadingState / ErrorRetry / EmptyState，BR-A6.1)
- [ ] **9.13 UI 屏幕汇总** → `aidlc-docs/construction/android/code/07-screens.md`

## Step 10: 构建验证 (编译)
- [ ] 10.1 `./gradlew :app:assembleDebug` 编译通过 (或 `compileDebugKotlin`)
- [ ] 10.2 修复编译错误，确认无重复文件 (无 `Xxx_new.kt` / `Xxx_modified.kt`)
- [ ] **10.3 生成单元总览** → `aidlc-docs/construction/android/code/00-summary.md` (含改造/新增文件清单、设计帧映射、构建结果)

> **说明**：单元测试 (JUnit/Compose UI test) 统一在后续 **Build and Test** 阶段编写与执行，本单元以编译通过为验收标准。

---

## 文件变更清单 (预期)

### 改造 (Modify in-place)
| 文件 | 变更 |
|------|------|
| `data/model/Product.kt`、`Order.kt`、`User.kt`、`PointsTransaction.kt` | 字段对齐后端 |
| `data/remote/ApiService.kt` | 路径/签名/`ApiResult` 重写 |
| `data/repository/AuthRepository.kt`、`ShopRepository.kt` | 改用 TokenStore / 分页 / 解包 |
| `di/AppModule.kt` | 注入 TokenStore + AuthInterceptor |
| `ui/navigation/Routes.kt`、`AppNavGraph.kt` | 补全参数 / ViewModel 接入 |
| `MainActivity.kt`、`ui/AwsomeShopRoot.kt` | 启动鉴权路由 |
| `ui/screens/**/*.kt` (11 屏) | 对齐设计帧 + ViewModel |
| `ui/components/BottomNavBar.kt`、`CategoryChip.kt` | 适配 |

### 新增 (Create)
| 文件 | 用途 |
|------|------|
| `data/local/TokenStore.kt` | Token/UserInfo 同步存储 |
| `data/remote/AuthInterceptor.kt` | Bearer Token 拦截器 |
| `data/remote/SafeApiCall.kt` | 响应解包 + ApiException |
| `data/model/PointsBalance.kt`、`Category.kt`、`ApiResult.kt`、`PageResult.kt`、`CreateOrderRequest.kt` | 数据模型 |
| `ui/viewmodel/*.kt` (6~7 个) | 各屏 ViewModel |
| `ui/components/LoadingState.kt`、`ErrorRetry.kt`、`EmptyState.kt` | 通用 UI 状态组件 |

---

## 故事覆盖 (US-E01~E11)

| 故事 | 说明 | 覆盖步骤 |
|------|------|---------|
| US-E01 | 登录 | 7.1, 9.1, 5.1 |
| US-E02 | 查看个人资料 | 9.11, 5.1 |
| US-E03~E05 | 浏览商品/分类/详情 | 7.2, 7.3, 9.2, 9.3, 5.2 |
| US-E06~E08 | 兑换流程 (配送/确认/成功) | 7.4, 9.4, 9.5, 9.6, 5.2 |
| US-E09 | 兑换记录/订单详情 | 7.5, 9.7, 9.8, 5.2 |
| US-E10~E11 | 积分余额/积分记录 | 7.6, 9.9, 9.10, 5.2 |

---

> **本计划共 10 个 Step (约 40 个可勾选子项)，覆盖数据/网络/仓库/DI/ViewModel/导航/12 个屏幕 + 构建验证。**
> 经批准后进入 PART 2，按 Step 顺序执行，每完成一项立即勾选 [x]。
