package com.awsome.shop.data.remote

import com.awsome.shop.data.model.ApiResult
import com.awsome.shop.data.model.Category
import com.awsome.shop.data.model.CreateOrderRequest
import com.awsome.shop.data.model.ListOrderRequest
import com.awsome.shop.data.model.ListProductRequest
import com.awsome.shop.data.model.LoginRequest
import com.awsome.shop.data.model.LoginResponse
import com.awsome.shop.data.model.Order
import com.awsome.shop.data.model.PageResult
import com.awsome.shop.data.model.PointsBalance
import com.awsome.shop.data.model.PointsTransaction
import com.awsome.shop.data.model.Product
import com.awsome.shop.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 后端 API 接口。
 *
 * baseUrl 已含 `/api/` (BuildConfig.BASE_URL = http://10.0.2.2:8080/api/)，
 * 因此此处路径以 `v1/...` 开头，最终拼为 `/api/v1/...`。
 * 所有响应统一包装为 [ApiResult]。
 */
interface ApiService {

    // ---- 认证 (public) ----
    @POST("v1/public/auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResult<LoginResponse>

    @GET("v1/user/profile")
    suspend fun getProfile(): ApiResult<User>

    // ---- 商品 (public) ----
    @POST("v1/public/product/list")
    suspend fun getProducts(@Body request: ListProductRequest): ApiResult<PageResult<Product>>

    @GET("v1/public/product/{id}")
    suspend fun getProductDetail(@Path("id") id: Long): ApiResult<Product>

    @GET("v1/public/category/list")
    suspend fun getCategories(): ApiResult<List<Category>>

    // ---- 订单 ----
    @POST("v1/order/create")
    suspend fun createOrder(@Body request: CreateOrderRequest): ApiResult<Order>

    @POST("v1/order/list")
    suspend fun getOrders(@Body request: ListOrderRequest): ApiResult<PageResult<Order>>

    @GET("v1/order/{id}")
    suspend fun getOrderDetail(@Path("id") id: Long): ApiResult<Order>

    // ---- 积分 ----
    @GET("v1/points/balance")
    suspend fun getPointsBalance(): ApiResult<PointsBalance>

    @GET("v1/points/transactions")
    suspend fun getPointsTransactions(
        @Query("type") type: String? = null,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20,
    ): ApiResult<PageResult<PointsTransaction>>
}
