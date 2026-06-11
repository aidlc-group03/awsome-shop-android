package com.awsome.shop.data.model

import kotlinx.serialization.Serializable

/**
 * 创建订单请求 — 对齐后端字段 (BR-A3)。
 */
@Serializable
data class CreateOrderRequest(
    val productId: Long,
    val quantity: Int = 1,
    val recipientName: String,
    val recipientPhone: String,
    val recipientAddress: String,
)

/**
 * 商品列表分页请求 (BR-A2.5)。
 */
@Serializable
data class ListProductRequest(
    val category: String? = null,
    val keyword: String? = null,
    val page: Int = 1,
    val size: Int = 20,
)

/**
 * 订单列表分页请求 (BR-A4.3)。
 */
@Serializable
data class ListOrderRequest(
    val status: String? = null,
    val page: Int = 1,
    val size: Int = 20,
)
