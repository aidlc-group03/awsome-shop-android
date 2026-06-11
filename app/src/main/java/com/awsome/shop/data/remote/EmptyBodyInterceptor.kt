package com.awsome.shop.data.remote

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import javax.inject.Inject

/**
 * 为「无请求体的 POST」补上空 JSON body 与 `Content-Type: application/json`。
 *
 * 后端网关要求 POST 携带 `Content-Type: application/json`，否则返回 500。
 * Retrofit 对没有 `@Body` 的 `@POST`（如积分余额、分类、地址列表、登出）发送的是
 * 长度为 0 且不带 Content-Type 的请求体，会触发后端 500。此处统一补 `{}`。
 */
class EmptyBodyInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val body = request.body
        val needsEmptyBody = request.method == "POST" &&
            (body == null || body.contentLength() == 0L)

        val newRequest = if (needsEmptyBody) {
            request.newBuilder()
                .method(request.method, EMPTY_JSON.toRequestBody(JSON_MEDIA_TYPE))
                .build()
        } else {
            request
        }
        return chain.proceed(newRequest)
    }

    private companion object {
        const val EMPTY_JSON = "{}"
        val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()
    }
}
