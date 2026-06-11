package com.awsome.shop.data.remote

import com.awsome.shop.data.local.TokenStore
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * 自动为请求附加 `Authorization: Bearer <token>` (BR-A1.4)。
 * 收到 401 时清除本地认证状态 (BR-A1.2)，UI 层据 [TokenStore.isLoggedIn] 跳转登录。
 */
class AuthInterceptor @Inject constructor(
    private val tokenStore: TokenStore,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenStore.getToken()
        val request = if (!token.isNullOrEmpty()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }

        val response = chain.proceed(request)
        if (response.code == 401) {
            tokenStore.clear()
        }
        return response
    }
}
