package com.awsome.shop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awsome.shop.data.model.AddressRequest
import com.awsome.shop.data.model.CreateRedemptionRequest
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
 * 兑换 ViewModel (BR-A3：创建地址 → 创建订单 + 防重复提交)。
 *
 * 后端下单需要 addressId，因此先调用收货地址 create 拿到 id，再创建兑换订单。
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
        recipientRegion: String,
        recipientDetail: String,
    ) {
        // 防重复提交 (BR-A3.4)。
        if (_uiState.value.isSubmitting) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, error = null) }
            // 1) 创建收货地址，拿到 addressId
            shopRepository.createAddress(
                AddressRequest(
                    name = recipientName,
                    phone = recipientPhone,
                    region = recipientRegion,
                    detail = recipientDetail,
                    isDefault = false,
                )
            ).onSuccess { address ->
                // 2) 用 addressId 创建兑换订单
                shopRepository.createOrder(CreateRedemptionRequest(productId = productId, addressId = address.id))
                    .onSuccess { order ->
                        _uiState.update { it.copy(isSubmitting = false, orderResult = order) }
                    }
                    .onFailure { e ->
                        _uiState.update { it.copy(isSubmitting = false, error = e.toUserMessage()) }
                    }
            }.onFailure { e ->
                _uiState.update { it.copy(isSubmitting = false, error = e.toUserMessage()) }
            }
        }
    }

    fun consumeError() {
        _uiState.update { it.copy(error = null) }
    }
}
