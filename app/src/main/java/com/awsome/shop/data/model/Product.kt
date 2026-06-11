package com.awsome.shop.data.model

import kotlinx.serialization.Serializable

/**
 * 商品模型 — 对齐后端 product 字段。
 */
@Serializable
data class Product(
    val id: Long,
    val name: String,
    val sku: String = "",
    val category: String = "",
    val brand: String? = null,
    val pointsPrice: Int = 0,
    val marketPrice: Double? = null,
    val stock: Int = 0,
    val soldCount: Int = 0,
    val status: Int = 1,
    val description: String? = null,
    val imageUrl: String? = null,
    val subtitle: String? = null,
    val deliveryMethod: String? = null,
    val serviceGuarantee: String? = null,
    val promotion: String? = null,
    val colors: String? = null,
    val specs: List<SpecItem>? = null,
) {
    /** 库存为 0 视为已兑完 (BR-A2.4)。 */
    val inStock: Boolean get() = stock > 0
}

@Serializable
data class SpecItem(
    val key: String,
    val value: String,
)
