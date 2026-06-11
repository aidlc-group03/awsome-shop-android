package com.awsome.shop.data.remote

import com.awsome.shop.data.model.Address
import com.awsome.shop.data.model.AddressRequest
import com.awsome.shop.data.model.ApiResult
import com.awsome.shop.data.model.Category
import com.awsome.shop.data.model.CreateRedemptionRequest
import com.awsome.shop.data.model.IdRequest
import com.awsome.shop.data.model.ListProductRequest
import com.awsome.shop.data.model.LoginRequest
import com.awsome.shop.data.model.LoginResponse
import com.awsome.shop.data.model.Order
import com.awsome.shop.data.model.OrderListRequest
import com.awsome.shop.data.model.PageResult
import com.awsome.shop.data.model.PointsBalance
import com.awsome.shop.data.model.PointsTransaction
import com.awsome.shop.data.model.Product
import com.awsome.shop.data.model.TransactionQueryRequest
import com.awsome.shop.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * 后端 API 接口（CloudFront 网关）。
 *
 * baseUrl 已含 `/api/` (BuildConfig.BASE_URL = https://dvxdma8v31u3f.cloudfront.net/api/)，
 * 因此此处路径以 `v1/...` 开头，最终拼为 `/api/v1/...`。
 * 所有响应统一包装为 [ApiResult]。
 */
interface ApiService {

    // ---- 认证 ----
    @POST("v1/public/auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResult<LoginResponse>

    @GET("v1/auth/profile")
    suspend fun getProfile(): ApiResult<User>

    @POST("v1/auth/logout")
    suspend fun logout(): ApiResult<Unit>

    // ---- 商品（public）----
    @POST("v1/public/product/list")
    suspend fun getProducts(@Body request: ListProductRequest): ApiResult<PageResult<Product>>

    @POST("v1/public/product/detail")
    suspend fun getProductDetail(@Body request: IdRequest): ApiResult<Product>

    @POST("v1/public/product/categories")
    suspend fun getCategories(): ApiResult<List<Category>>

    // ---- 订单 ----
    @POST("v1/order/create")
    suspend fun createOrder(@Body request: CreateRedemptionRequest): ApiResult<Order>

    @POST("v1/order/list")
    suspend fun getOrders(@Body request: OrderListRequest): ApiResult<PageResult<Order>>

    @POST("v1/order/detail")
    suspend fun getOrderDetail(@Body request: IdRequest): ApiResult<Order>

    // ---- 收货地址 ----
    @POST("v1/order/address/list")
    suspend fun getAddresses(): ApiResult<List<Address>>

    @POST("v1/order/address/create")
    suspend fun createAddress(@Body request: AddressRequest): ApiResult<Address>

    // ---- 积分 ----
    @POST("v1/point/balance")
    suspend fun getPointsBalance(): ApiResult<PointsBalance>

    @POST("v1/point/transactions")
    suspend fun getPointsTransactions(@Body request: TransactionQueryRequest): ApiResult<PageResult<PointsTransaction>>
}
