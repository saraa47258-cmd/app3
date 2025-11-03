package com.example.app3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app3.data.model.Product
import com.example.app3.data.model.ProductWithProgress
import com.example.app3.data.repository.WishListRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WishListViewModel(private val repository: WishListRepository) : ViewModel() {

    sealed class SortOption {
        object Recent : SortOption()
        object Priority : SortOption()
        object Price : SortOption()
    }

    private val _sortOption = MutableStateFlow<SortOption>(SortOption.Recent)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    val products: Flow<List<ProductWithProgress>> = _sortOption.flatMapLatest { option ->
        when (option) {
            is SortOption.Recent -> repository.getProductsWithProgress()
            is SortOption.Priority -> repository.getProductsWithProgress()
                .map { list -> list.sortedByDescending { it.product.priority.value } }
            is SortOption.Price -> repository.getProductsWithProgress()
                .map { list -> list.sortedBy { it.product.price } }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val budget = repository.getBudget()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val totalValue = repository.getTotalWishlistValue()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    val productCount = repository.getActiveProductCount()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }

    fun markAsPurchased(productId: Long) {
        viewModelScope.launch {
            repository.markAsPurchased(productId)
        }
    }
}

class WishListViewModelFactory(private val repository: WishListRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WishListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WishListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


