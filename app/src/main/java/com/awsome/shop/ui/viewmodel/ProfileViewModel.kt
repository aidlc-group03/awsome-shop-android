package com.awsome.shop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awsome.shop.data.model.PointsBalance
import com.awsome.shop.data.model.User
import com.awsome.shop.data.repository.AuthRepository
import com.awsome.shop.data.repository.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 个人中心 ViewModel (US-E02, BR-A1.5)。
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val shopRepository: ShopRepository,
) : ViewModel() {

    data class UiState(
        val user: User? = null,
        val balance: PointsBalance? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val loggedOut: Boolean = false,
    )

    private val _uiState = MutableStateFlow(UiState(user = authRepository.cachedUser()))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            authRepository.getProfile()
                .onSuccess { user -> _uiState.update { it.copy(isLoading = false, user = user) } }
                .onFailure { _uiState.update { it.copy(isLoading = false) } }
            shopRepository.getPointsBalance()
                .onSuccess { balance -> _uiState.update { it.copy(balance = balance) } }
        }
    }

    /** 登出，清除本地认证数据 (BR-A1.5)。 */
    fun logout() {
        authRepository.logout()
        _uiState.update { it.copy(loggedOut = true) }
    }
}
