package com.awsome.shop.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.awsome.shop.data.remote.ApiService
import com.awsome.shop.data.remote.LoginRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val dataStore: DataStore<Preferences>,
) {
    private val tokenKey = stringPreferencesKey("auth_token")

    val isLoggedIn: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[tokenKey] != null
    }

    suspend fun login(username: String, password: String): Result<Unit> {
        return try {
            val response = apiService.login(LoginRequest(username, password))
            dataStore.edit { prefs ->
                prefs[tokenKey] = response.token
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        dataStore.edit { prefs ->
            prefs.remove(tokenKey)
        }
    }

    suspend fun getToken(): String? {
        var token: String? = null
        dataStore.data.collect { prefs ->
            token = prefs[tokenKey]
        }
        return token
    }
}
