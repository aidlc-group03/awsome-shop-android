package com.awsome.shop.data.model

import kotlinx.serialization.Serializable

/**
 * 商品模型 — 对齐后端 ProductDTO。
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
    /** 后端返回为对象数组（键值对映射列表）。 */
    val specs: List<Map<String, String>>? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
) {
    /** 库存为 0 视为已兑完 (BR-A2.4)。 */
    val inStock: Boolean get() = stock > 0

    /** 将 specs 列表展平为 (键, 值) 对，供 UI 渲染。 */
    val specEntries: List<Pair<String, String>>
        get() = specs.orEmpty().flatMap { it.entries.map { e -> e.key to e.value } }
}
