package com.awsome.shop.data.remote

import com.awsome.shop.data.model.Address
import com.awsome.shop.data.model.Order
import com.awsome.shop.data.model.PointsTransaction
import com.awsome.shop.data.model.Product
import com.awsome.shop.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("user/profile")
    suspend fun getProfile(): User

    @GET("products")
    suspend fun getProducts(
        @Query("category") category: String? = null,
        @Query("page") page: Int = 1,
    ): List<Product>

    @GET("products/{id}")
    suspend fun getProductDetail(@Path("id") id: String): Product

    @POST("orders")
    suspend fun createOrder(@Body request: CreateOrderRequest): Order

    @GET("orders")
    suspend fun getOrders(@Query("status") status: String? = null): List<Order>

    @GET("orders/{id}")
    suspend fun getOrderDetail(@Path("id") id: String): Order

    @GET("points/transactions")
    suspend fun getPointsTransactions(
        @Query("type") type: String? = null,
    ): List<PointsTransaction>

    @GET("addresses")
    suspend fun getAddresses(): List<Address>

    @POST("addresses")
    suspend fun createAddress(@Body address: Address): Address
}

@kotlinx.serialization.Serializable
data class LoginRequest(val username: String, val password: String)

@kotlinx.serialization.Serializable
data class LoginResponse(val token: String, val user: User)

@kotlinx.serialization.Serializable
data class CreateOrderRequest(val productId: String, val addressId: String)
