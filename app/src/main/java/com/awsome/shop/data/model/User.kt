package com.awsome.shop.data.model

import kotlinx.serialization.Serializable

/**
 * 用户模型 — 对齐后端 UserDTO。
 */
@Serializable
data class User(
    val id: Long,
    val username: String,
    val displayName: String = "",
    val role: String = "EMPLOYEE",
    val employeeId: String? = null,
    val department: String? = null,
    val title: String? = null,
    val avatar: String? = null,
)
