package com.example.app3.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget")
data class Budget(
    @PrimaryKey
    val id: Long = 1, // Only one budget entry
    val monthlyIncome: Double = 0.0,
    val monthlySaving: Double = 0.0,
    val fixedExpenses: Double = 0.0,
    val currency: String = "ر.س", // Default to Saudi Riyal
    val lastUpdated: Long = System.currentTimeMillis()
) {
    val availableForSaving: Double
        get() = monthlyIncome - fixedExpenses
}


