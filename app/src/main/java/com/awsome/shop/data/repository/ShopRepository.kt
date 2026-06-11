package com.awsome.shop.data.repository

import com.awsome.shop.data.model.Address
import com.awsome.shop.data.model.AddressRequest
import com.awsome.shop.data.model.Category
import com.awsome.shop.data.model.CreateRedemptionRequest
import com.awsome.shop.data.model.IdRequest
import com.awsome.shop.data.model.ListProductRequest
import com.awsome.shop.data.model.Order
import com.awsome.shop.data.model.OrderListRequest
import com.awsome.shop.data.model.PageResult
import com.awsome.shop.data.model.PointsBalance
import com.awsome.shop.data.model.PointsTransaction
import com.awsome.shop.data.model.Product
import com.awsome.shop.data.model.TransactionQueryRequest
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
        size: Int = 10,
    ): Result<PageResult<Product>> =
        safeApiCall { apiService.getProducts(ListProductRequest(page = page, size = size, category = category)) }

    suspend fun getProductDetail(id: Long): Result<Product> =
        safeApiCall { apiService.getProductDetail(IdRequest(id)) }

    suspend fun getCategories(): Result<List<Category>> =
        safeApiCall { apiService.getCategories() }

    suspend fun createOrder(request: CreateRedemptionRequest): Result<Order> =
        safeApiCall { apiService.createOrder(request) }

    suspend fun getOrders(
        status: String? = null,
        page: Int = 1,
        size: Int = 10,
    ): Result<PageResult<Order>> =
        safeApiCall { apiService.getOrders(OrderListRequest(page = page, size = size, status = status)) }

    suspend fun getOrderDetail(id: Long): Result<Order> =
        safeApiCall { apiService.getOrderDetail(IdRequest(id)) }

    // ---- 收货地址 ----
    suspend fun getAddresses(): Result<List<Address>> =
        safeApiCall { apiService.getAddresses() }

    suspend fun createAddress(request: AddressRequest): Result<Address> =
        safeApiCall { apiService.createAddress(request) }

    // ---- 积分 ----
    suspend fun getPointsBalance(): Result<PointsBalance> =
        safeApiCall { apiService.getPointsBalance() }

    suspend fun getPointsTransactions(
        type: String? = null,
        page: Int = 1,
        size: Int = 20,
    ): Result<PageResult<PointsTransaction>> =
        safeApiCall { apiService.getPointsTransactions(TransactionQueryRequest(page = page, size = size, type = type)) }
}
