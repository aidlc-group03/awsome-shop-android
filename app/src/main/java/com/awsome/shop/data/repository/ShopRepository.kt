package com.awsome.shop.data.repository

import com.awsome.shop.data.model.Order
import com.awsome.shop.data.model.PointsTransaction
import com.awsome.shop.data.model.Product
import com.awsome.shop.data.model.User
import com.awsome.shop.data.remote.ApiService
import com.awsome.shop.data.remote.CreateOrderRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getProducts(category: String? = null): Result<List<Product>> = runCatching {
        apiService.getProducts(category = category)
    }

    suspend fun getProductDetail(id: String): Result<Product> = runCatching {
        apiService.getProductDetail(id)
    }

    suspend fun getProfile(): Result<User> = runCatching {
        apiService.getProfile()
    }

    suspend fun createOrder(productId: String, addressId: String): Result<Order> = runCatching {
        apiService.createOrder(CreateOrderRequest(productId, addressId))
    }

    suspend fun getOrders(status: String? = null): Result<List<Order>> = runCatching {
        apiService.getOrders(status)
    }

    suspend fun getOrderDetail(id: String): Result<Order> = runCatching {
        apiService.getOrderDetail(id)
    }

    suspend fun getPointsTransactions(type: String? = null): Result<List<PointsTransaction>> = runCatching {
        apiService.getPointsTransactions(type)
    }
}
