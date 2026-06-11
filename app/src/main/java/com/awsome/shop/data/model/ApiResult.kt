package com.awsome.shop.data.model

import kotlinx.serialization.Serializable

/**
 * 后端统一响应包装。约定 code == 0 表示成功 (BR-A6.4)。
 */
@Serializable
data class ApiResult<T>(
    val code: Int = -1,
    val message: String = "",
    val data: T? = null,
)
