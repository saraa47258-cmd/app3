package com.example.app3.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offers")
data class Offer(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productName: String,
    val discountPercentage: Int,
    val originalPrice: Double,
    val discountedPrice: Double,
    val offerUrl: String? = null,
    val expiryDate: Long? = null,
    val source: String = "عرض محلي",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)


