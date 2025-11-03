package com.example.app3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app3.data.model.Offer
import com.example.app3.data.repository.WishListRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class OffersViewModel(private val repository: WishListRepository) : ViewModel() {

    val offers = repository.getAllActiveOffers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Deactivate expired offers on initialization
        viewModelScope.launch {
            repository.deactivateExpiredOffers()
        }
        
        // Add some sample offers for demonstration
        addSampleOffers()
    }

    fun deleteOffer(offer: Offer) {
        viewModelScope.launch {
            repository.deleteOffer(offer)
        }
    }

    private fun addSampleOffers() {
        viewModelScope.launch {
            val sampleOffers = listOf(
                Offer(
                    productName = "آيفون 15 برو",
                    discountPercentage = 15,
                    originalPrice = 5000.0,
                    discountedPrice = 4250.0,
                    offerUrl = "https://example.com",
                    expiryDate = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000),
                    source = "متجر الكتروني"
                ),
                Offer(
                    productName = "سماعات AirPods Pro",
                    discountPercentage = 20,
                    originalPrice = 999.0,
                    discountedPrice = 799.0,
                    offerUrl = "https://example.com",
                    expiryDate = System.currentTimeMillis() + (15L * 24 * 60 * 60 * 1000),
                    source = "عرض خاص"
                ),
                Offer(
                    productName = "ساعة Apple Watch",
                    discountPercentage = 10,
                    originalPrice = 1800.0,
                    discountedPrice = 1620.0,
                    offerUrl = "https://example.com",
                    expiryDate = System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000),
                    source = "تخفيض موسمي"
                )
            )
            
            // Only add if no offers exist
            if (offers.value.isEmpty()) {
                repository.insertOffers(sampleOffers)
            }
        }
    }
}

class OffersViewModelFactory(private val repository: WishListRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OffersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OffersViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


