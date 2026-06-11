package com.awsome.shop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awsome.shop.data.model.Order
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
 * 订单列表 ViewModel (BR-A4：列表 + 状态筛选)。
 */
@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val shopRepository: ShopRepository,
) : ViewModel() {

    data class UiState(
        val orders: List<Order> = emptyList(),
        val selectedStatus: String? = null,
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,
        val error: String? = null,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders(status: String? = _uiState.value.selectedStatus) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            shopRepository.getOrders(status = status)
                .onSuccess { page ->
                    _uiState.update { it.copy(isLoading = false, orders = page.list) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.toUserMessage()) }
                }
        }
    }

    fun selectStatus(status: String?) {
        _uiState.update { it.copy(selectedStatus = status) }
        loadOrders(status)
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }
            shopRepository.getOrders(status = _uiState.value.selectedStatus)
                .onSuccess { page ->
                    _uiState.update { it.copy(isRefreshing = false, orders = page.list) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isRefreshing = false, error = e.toUserMessage()) }
                }
        }
    }
}
