package com.awsome.shop.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awsome.shop.data.model.PointsBalance
import com.awsome.shop.data.model.Product
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
 * 商品详情 ViewModel (BR-A3.1：积分/库存校验)。
 */
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val shopRepository: ShopRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val productId: Long = savedStateHandle.get<Long>("productId") ?: 0L

    data class UiState(
        val product: Product? = null,
        val balance: PointsBalance? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
    ) {
        /** 积分是否充足 (BR-A3.1)。 */
        val canRedeem: Boolean
            get() = product != null && product.inStock &&
                (balance == null || balance.balance >= product.pointsPrice)
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadProduct()
    }

    fun loadProduct() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            shopRepository.getProductDetail(productId)
                .onSuccess { product ->
                    _uiState.update { it.copy(isLoading = false, product = product) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.toUserMessage()) }
                }
            // 拉取余额用于积分充足校验，失败不阻断详情展示。
            shopRepository.getPointsBalance()
                .onSuccess { balance -> _uiState.update { it.copy(balance = balance) } }
        }
    }
}
