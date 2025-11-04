package com.example.app3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app3.data.model.Priority
import com.example.app3.data.model.Product
import com.example.app3.data.repository.WishListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddProductViewModel(private val repository: WishListRepository) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _price = MutableStateFlow("")
    val price: StateFlow<String> = _price.asStateFlow()

    private val _category = MutableStateFlow("أخرى")
    val category: StateFlow<String> = _category.asStateFlow()

    private val _priority = MutableStateFlow(Priority.MEDIUM)
    val priority: StateFlow<Priority> = _priority.asStateFlow()

    private val _imageUri = MutableStateFlow<String?>(null)
    val imageUri: StateFlow<String?> = _imageUri.asStateFlow()

    private val _productUrl = MutableStateFlow("")
    val productUrl: StateFlow<String> = _productUrl.asStateFlow()

    private val _notes = MutableStateFlow("")
    val notes: StateFlow<String> = _notes.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

    fun updateName(value: String) {
        _name.value = value
    }

    fun updatePrice(value: String) {
        _price.value = value
    }

    fun updateCategory(value: String) {
        _category.value = value
    }

    fun updatePriority(value: Priority) {
        _priority.value = value
    }

    fun updateImageUri(value: String?) {
        _imageUri.value = value
    }

    fun updateProductUrl(value: String) {
        _productUrl.value = value
    }

    fun updateNotes(value: String) {
        _notes.value = value
    }

    fun isFormValid(): Boolean {
        return name.value.isNotBlank() && price.value.toDoubleOrNull() != null && price.value.toDouble() > 0
    }

    fun saveProduct(onSuccess: () -> Unit) {
        if (!isFormValid()) return

        viewModelScope.launch {
            _isSaving.value = true
            try {
                val product = Product(
                    name.value.trim(),
                    price.value.toDouble(),
                    category.value,
                    priority.value,
                    imageUri.value,
                    productUrl.value.ifBlank { null },
                    notes.value.ifBlank { null }
                )
                repository.insertProduct(product)
                _saveSuccess.value = true
                clearForm()
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isSaving.value = false
            }
        }
    }

    private fun clearForm() {
        _name.value = ""
        _price.value = ""
        _category.value = "أخرى"
        _priority.value = Priority.MEDIUM
        _imageUri.value = null
        _productUrl.value = ""
        _notes.value = ""
    }

    fun resetSaveSuccess() {
        _saveSuccess.value = false
    }
}

class AddProductViewModelFactory(private val repository: WishListRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddProductViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



