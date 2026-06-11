package com.awsome.shop.data.model

import kotlinx.serialization.Serializable

/**
 * 商品分类 (BR-A2.2)。
 */
@Serializable
data class Category(
    val id: Long,
    val name: String,
    val code: String? = null,
    val sortOrder: Int = 0,
)
