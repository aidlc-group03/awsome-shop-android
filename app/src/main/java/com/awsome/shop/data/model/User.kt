package com.awsome.shop.data.model

import kotlinx.serialization.Serializable

/**
 * 用户模型 — 对齐后端字段。
 */
@Serializable
data class User(
    val id: Long,
    val username: String,
    val displayName: String = "",
    val email: String? = null,
    val role: String = "employee",
    val avatarUrl: String? = null,
)
