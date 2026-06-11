package com.awsome.shop.data.model

import kotlinx.serialization.Serializable

/**
 * 后端统一响应包装。约定 code == "SUCCESS" 表示成功 (BR-A6.4)。
 * 注意：真实后端 code 为字符串（如 "SUCCESS" / 业务错误码），非整数。
 */
@Serializable
data class ApiResult<T>(
    val code: String = "",
    val message: String = "",
    val data: T? = null,
) {
    val isSuccess: Boolean get() = code.equals("SUCCESS", ignoreCase = true) || code == "0" || code == "200"
}
