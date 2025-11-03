package com.example.app3.data.local

import androidx.room.*
import com.example.app3.data.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE isPurchased = 0 ORDER BY createdAt DESC")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE isPurchased = 0 ORDER BY priority DESC, createdAt DESC")
    fun getProductsByPriority(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE isPurchased = 0 ORDER BY price ASC")
    fun getProductsByPrice(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE isPurchased = 1 ORDER BY purchasedDate DESC")
    fun getPurchasedProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: Long): Product?

    @Query("SELECT * FROM products WHERE id = :productId")
    fun getProductByIdFlow(productId: Long): Flow<Product?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun deleteProductById(productId: Long)

    @Query("SELECT COUNT(*) FROM products WHERE isPurchased = 0")
    fun getActiveProductCount(): Flow<Int>

    @Query("SELECT SUM(price) FROM products WHERE isPurchased = 0")
    fun getTotalWishlistValue(): Flow<Double?>

    @Query("UPDATE products SET isPurchased = 1, purchasedDate = :purchaseDate WHERE id = :productId")
    suspend fun markAsPurchased(productId: Long, purchaseDate: Long = System.currentTimeMillis())
}


