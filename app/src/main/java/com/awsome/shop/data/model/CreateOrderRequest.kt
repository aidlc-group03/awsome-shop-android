package com.awsome.shop.data.model

import kotlinx.serialization.Serializable

/** 通用 id 请求体（商品详情/订单详情等）。 */
@Serializable
data class IdRequest(val id: Long)

/** 积分兑换下单请求 — 需要收货地址 id (operatorId 由 Gateway 注入)。 */
@Serializable
data class CreateRedemptionRequest(
    val productId: Long,
    val addressId: Long,
)

/** 商品列表分页请求 (BR-A2.5)。 */
@Serializable
data class ListProductRequest(
    val page: Int = 1,
    val size: Int = 10,
    val name: String? = null,
    val category: String? = null,
)

/** 订单列表分页请求 (BR-A4.3)。status: PENDING/SHIPPED/COMPLETED/CANCELLED。 */
@Serializable
data class OrderListRequest(
    val page: Int = 1,
    val size: Int = 10,
    val status: String? = null,
)

/** 积分交易记录查询请求 (BR-A5.4)。 */
@Serializable
data class TransactionQueryRequest(
    val page: Int = 1,
    val size: Int = 20,
    val type: String? = null,
)
