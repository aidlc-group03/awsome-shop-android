package com.awsome.shop.data.local

import android.content.Context
import com.awsome.shop.data.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Token / 用户信息本地存储 (BR-A1.3)。
 *
 * 使用 SharedPreferences 实现 **同步** 读写，供 OkHttp [AuthInterceptor] 在拦截链中
 * 直接读取 token (不可挂起)。
 */
@Singleton
class TokenStore @Inject constructor(
    @ApplicationContext context: Context,
    private val json: Json,
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun saveUser(user: User) {
        prefs.edit().putString(KEY_USER, json.encodeToString(User.serializer(), user)).apply()
    }

    fun getUser(): User? {
        val raw = prefs.getString(KEY_USER, null) ?: return null
        return runCatching { json.decodeFromString(User.serializer(), raw) }.getOrNull()
    }

    fun isLoggedIn(): Boolean = !getToken().isNullOrEmpty()

    /** 清除全部认证数据 (BR-A1.5)。 */
    fun clear() {
        prefs.edit().clear().apply()
    }

    private companion object {
        const val PREFS_NAME = "auth"
        const val KEY_TOKEN = "auth_token"
        const val KEY_USER = "user_info"
    }
}
