package com.awsome.shop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awsome.shop.data.model.PointsBalance
import com.awsome.shop.data.model.PointsTransaction
import com.awsome.shop.data.remote.toUserMessage
import com.awsome.shop.data.repository.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 积分中心 ViewModel (BR-A5：余额 + 交易记录 + 类型筛选)。
 */
@HiltViewModel
class PointsViewModel @Inject constructor(
    private val shopRepository: ShopRepository,
) : ViewModel() {

    data class UiState(
        val balance: PointsBalance? = null,
        val transactions: List<PointsTransaction> = emptyList(),
        val selectedType: String? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadBalance()
        loadTransactions()
    }

    fun loadBalance() {
        viewModelScope.launch {
            shopRepository.getPointsBalance()
                .onSuccess { balance -> _uiState.update { it.copy(balance = balance) } }
                .onFailure { e -> _uiState.update { it.copy(error = e.toUserMessage()) } }
        }
    }

    fun loadTransactions(type: String? = _uiState.value.selectedType) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            shopRepository.getPointsTransactions(type = type)
                .onSuccess { page ->
                    _uiState.update { it.copy(isLoading = false, transactions = page.list) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.toUserMessage()) }
                }
        }
    }

    fun selectType(type: String?) {
        _uiState.update { it.copy(selectedType = type) }
        loadTransactions(type)
    }
}
