package com.awsome.shop.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
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
 * 订单详情 ViewModel (BR-A4.4)。
 */
@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val shopRepository: ShopRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val orderId: Long = savedStateHandle.get<Long>("orderId") ?: 0L

    data class UiState(
        val order: Order? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadOrder()
    }

    fun loadOrder() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            shopRepository.getOrderDetail(orderId)
                .onSuccess { order ->
                    _uiState.update { it.copy(isLoading = false, order = order) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.toUserMessage()) }
                }
        }
    }
}
