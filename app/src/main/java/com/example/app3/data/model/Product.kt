package com.example.app3.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val price: Double,
    val imageUri: String? = null,
    val category: String = "أخرى",
    val priority: Priority = Priority.MEDIUM,
    val productUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isPurchased: Boolean = false,
    val purchasedDate: Long? = null,
    val notes: String? = null
)

enum class Priority(val displayName: String, val value: Int) {
    LOW("منخفضة", 1),
    MEDIUM("متوسطة", 2),
    HIGH("عالية", 3)
}

data class ProductWithProgress(
    val product: Product,
    val monthsNeeded: Int,
    val progressPercentage: Float,
    val estimatedDate: String
)


