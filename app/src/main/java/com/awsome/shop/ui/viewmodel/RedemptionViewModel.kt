package com.awsome.shop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awsome.shop.data.model.CreateOrderRequest
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
 * 兑换 ViewModel (BR-A3：创建订单 + 防重复提交)。
 */
@HiltViewModel
class RedemptionViewModel @Inject constructor(
    private val shopRepository: ShopRepository,
) : ViewModel() {

    data class UiState(
        val isSubmitting: Boolean = false,
        val orderResult: Order? = null,
        val error: String? = null,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun createOrder(
        productId: Long,
        recipientName: String,
        recipientPhone: String,
        recipientAddress: String,
        quantity: Int = 1,
    ) {
        // 防重复提交 (BR-A3.4)。
        if (_uiState.value.isSubmitting) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, error = null) }
            shopRepository.createOrder(
                CreateOrderRequest(
                    productId = productId,
                    quantity = quantity,
                    recipientName = recipientName,
                    recipientPhone = recipientPhone,
                    recipientAddress = recipientAddress,
                )
            ).onSuccess { order ->
                _uiState.update { it.copy(isSubmitting = false, orderResult = order) }
            }.onFailure { e ->
                _uiState.update { it.copy(isSubmitting = false, error = e.toUserMessage()) }
            }
        }
    }

    fun consumeError() {
        _uiState.update { it.copy(error = null) }
    }
}
