package com.awsome.shop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awsome.shop.data.model.Category
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
 * 首页商城 ViewModel (BR-A2)。
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val shopRepository: ShopRepository,
) : ViewModel() {

    data class UiState(
        val products: List<Product> = emptyList(),
        val categories: List<Category> = emptyList(),
        val selectedCategory: String? = null,
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,
        val error: String? = null,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadCategories()
        loadProducts()
    }

    fun loadProducts(category: String? = _uiState.value.selectedCategory) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            shopRepository.getProducts(category = category)
                .onSuccess { page ->
                    _uiState.update { it.copy(isLoading = false, products = page.list) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.toUserMessage()) }
                }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            shopRepository.getCategories()
                .onSuccess { list -> _uiState.update { it.copy(categories = list) } }
        }
    }

    fun selectCategory(category: String?) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadProducts(category)
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }
            shopRepository.getProducts(category = _uiState.value.selectedCategory)
                .onSuccess { page ->
                    _uiState.update { it.copy(isRefreshing = false, products = page.list) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isRefreshing = false, error = e.toUserMessage()) }
                }
        }
    }
}
