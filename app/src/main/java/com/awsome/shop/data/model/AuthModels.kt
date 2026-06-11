package com.awsome.shop.data.model

import kotlinx.serialization.Serializable

/**
 * 登录请求 (BR-A1)。
 */
@Serializable
data class LoginRequest(
    val username: String,
    val password: String,
)

/**
 * 登录响应 — token + 用户信息 (BR-A1.3)。
 */
@Serializable
data class LoginResponse(
    val token: String,
    val user: User,
)
