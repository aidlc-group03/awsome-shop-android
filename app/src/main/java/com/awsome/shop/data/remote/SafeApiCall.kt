package com.awsome.shop.data.remote

import com.awsome.shop.data.model.ApiResult
import retrofit2.HttpException
import java.io.IOException

/**
 * 业务异常 — 承载后端返回的 code/message (BR-A6.4)。
 */
class ApiException(
    val code: Int,
    override val message: String,
) : Exception(message)

/**
 * 统一解包 [ApiResult]：
 * - code == 0 且 data != null → [Result.success]
 * - 业务错误 → [ApiException]
 * - 网络/超时/HTTP 错误 → 原始异常，由上层映射友好文案 (BR-A6.1~A6.5)
 */
suspend fun <T> safeApiCall(call: suspend () -> ApiResult<T>): Result<T> {
    return try {
        val response = call()
        if (response.code == 0 && response.data != null) {
            Result.success(response.data as T)
        } else {
            Result.failure(ApiException(response.code, response.message.ifEmpty { "请求失败" }))
        }
    } catch (e: ApiException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

/**
 * 将异常映射为用户可读的错误文案 (BR-A6.1~A6.4)。
 */
fun Throwable.toUserMessage(): String = when (this) {
    is ApiException -> message
    is java.net.SocketTimeoutException -> "请求超时，请重试"
    is IOException -> "网络连接失败"
    is HttpException -> when (code()) {
        in 500..599 -> "系统繁忙，请稍后重试"
        401 -> "登录已过期，请重新登录"
        else -> "请求失败 (${code()})"
    }
    else -> message ?: "未知错误"
}
