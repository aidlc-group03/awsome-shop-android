package com.awsome.shop.data.repository

import com.awsome.shop.data.model.Category
import com.awsome.shop.data.model.CreateOrderRequest
import com.awsome.shop.data.model.ListOrderRequest
import com.awsome.shop.data.model.ListProductRequest
import com.awsome.shop.data.model.Order
import com.awsome.shop.data.model.PageResult
import com.awsome.shop.data.model.PointsBalance
import com.awsome.shop.data.model.PointsTransaction
import com.awsome.shop.data.model.Product
import com.awsome.shop.data.remote.ApiService
import com.awsome.shop.data.remote.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 商城数据仓库 (BR-A2~A5)。所有方法通过 [safeApiCall] 解包 `ApiResult` 为 `Result`。
 */
@Singleton
class ShopRepository @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getProducts(
        category: String? = null,
        page: Int = 1,
        size: Int = 20,
    ): Result<PageResult<Product>> =
        safeApiCall { apiService.getProducts(ListProductRequest(category = category, page = page, size = size)) }

    suspend fun getProductDetail(id: Long): Result<Product> =
        safeApiCall { apiService.getProductDetail(id) }

    suspend fun getCategories(): Result<List<Category>> =
        safeApiCall { apiService.getCategories() }

    suspend fun createOrder(request: CreateOrderRequest): Result<Order> =
        safeApiCall { apiService.createOrder(request) }

    suspend fun getOrders(
        status: String? = null,
        page: Int = 1,
        size: Int = 20,
    ): Result<PageResult<Order>> =
        safeApiCall { apiService.getOrders(ListOrderRequest(status = status, page = page, size = size)) }

    suspend fun getOrderDetail(id: Long): Result<Order> =
        safeApiCall { apiService.getOrderDetail(id) }

    suspend fun getPointsBalance(): Result<PointsBalance> =
        safeApiCall { apiService.getPointsBalance() }

    suspend fun getPointsTransactions(
        type: String? = null,
        page: Int = 1,
        size: Int = 20,
    ): Result<PageResult<PointsTransaction>> =
        safeApiCall { apiService.getPointsTransactions(type = type, page = page, size = size) }
}
