package com.awsome.shop.data.model

import kotlinx.serialization.Serializable

/**
 * 商品分类 — 对齐后端 CategoryDTO (BR-A2.2)。
 */
@Serializable
data class Category(
    val id: Long,
    val name: String,
    val description: String? = null,
    val icon: String? = null,
    val sortOrder: Int = 0,
    val status: Int = 1,
)
