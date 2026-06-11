package com.awsome.shop.data.repository

import com.awsome.shop.data.local.TokenStore
import com.awsome.shop.data.model.LoginRequest
import com.awsome.shop.data.model.User
import com.awsome.shop.data.remote.ApiService
import com.awsome.shop.data.remote.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 认证仓库 (BR-A1)。改用 [TokenStore] 同步存储 token + 用户信息。
 */
@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenStore: TokenStore,
) {
    /** 登录成功后存储 token + user (BR-A1.3)。 */
    suspend fun login(username: String, password: String): Result<User> =
        safeApiCall { apiService.login(LoginRequest(username, password)) }
            .map { resp ->
                tokenStore.saveToken(resp.token)
                tokenStore.saveUser(resp.user)
                resp.user
            }

    /** 启动鉴权用：本地是否已登录 (BR-A1.1)。 */
    fun isLoggedIn(): Boolean = tokenStore.isLoggedIn()

    /** 本地缓存的用户信息。 */
    fun cachedUser(): User? = tokenStore.getUser()

    /** 登出，清除全部认证数据 (BR-A1.5)。 */
    fun logout() = tokenStore.clear()

    /** 拉取最新用户资料。 */
    suspend fun getProfile(): Result<User> =
        safeApiCall { apiService.getProfile() }
}
