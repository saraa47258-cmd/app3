package com.example.app3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app3.data.model.Budget
import com.example.app3.data.repository.WishListRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BudgetViewModel(private val repository: WishListRepository) : ViewModel() {

    private val _monthlyIncome = MutableStateFlow("")
    val monthlyIncome: StateFlow<String> = _monthlyIncome.asStateFlow()

    private val _monthlySaving = MutableStateFlow("")
    val monthlySaving: StateFlow<String> = _monthlySaving.asStateFlow()

    private val _fixedExpenses = MutableStateFlow("")
    val fixedExpenses: StateFlow<String> = _fixedExpenses.asStateFlow()

    private val _currency = MutableStateFlow("ر.س")
    val currency: StateFlow<String> = _currency.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    val budget = repository.getBudget()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val productsWithProgress = repository.getProductsWithProgress()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            budget.filterNotNull().collect { budget ->
                _monthlyIncome.value = budget.monthlyIncome.toString()
                _monthlySaving.value = budget.monthlySaving.toString()
                _fixedExpenses.value = budget.fixedExpenses.toString()
                _currency.value = budget.currency
            }
        }
    }

    fun updateMonthlyIncome(value: String) {
        _monthlyIncome.value = value
    }

    fun updateMonthlySaving(value: String) {
        _monthlySaving.value = value
    }

    fun updateFixedExpenses(value: String) {
        _fixedExpenses.value = value
    }

    fun updateCurrency(value: String) {
        _currency.value = value
    }

    fun calculateSuggestedSaving(): Double {
        val income = monthlyIncome.value.toDoubleOrNull() ?: 0.0
        val expenses = fixedExpenses.value.toDoubleOrNull() ?: 0.0
        return (income - expenses) * 0.3 // Suggest 30% of available income
    }

    fun saveBudget(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isSaving.value = true
            try {
                val budgetData = Budget(
                    id = 1,
                    monthlyIncome = monthlyIncome.value.toDoubleOrNull() ?: 0.0,
                    monthlySaving = monthlySaving.value.toDoubleOrNull() ?: 0.0,
                    fixedExpenses = fixedExpenses.value.toDoubleOrNull() ?: 0.0,
                    currency = currency.value,
                    lastUpdated = System.currentTimeMillis()
                )
                repository.insertBudget(budgetData)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun isFormValid(): Boolean {
        val income = monthlyIncome.value.toDoubleOrNull() ?: 0.0
        val saving = monthlySaving.value.toDoubleOrNull() ?: 0.0
        return income > 0 && saving >= 0
    }
}

class BudgetViewModelFactory(private val repository: WishListRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BudgetViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


