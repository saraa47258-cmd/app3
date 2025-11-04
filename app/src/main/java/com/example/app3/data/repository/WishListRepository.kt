package com.example.app3.data.repository

import com.example.app3.data.local.BudgetDao
import com.example.app3.data.local.OfferDao
import com.example.app3.data.local.ProductDao
import com.example.app3.data.model.Budget
import com.example.app3.data.model.Offer
import com.example.app3.data.model.Product
import com.example.app3.data.model.ProductWithProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

class WishListRepository(
    private val productDao: ProductDao,
    private val budgetDao: BudgetDao,
    private val offerDao: OfferDao
) {
    // Products
    fun getAllProducts(): Flow<List<Product>> = productDao.getAllProducts()
    
    fun getProductsByPriority(): Flow<List<Product>> = productDao.getProductsByPriority()
    
    fun getProductsByPrice(): Flow<List<Product>> = productDao.getProductsByPrice()
    
    fun getPurchasedProducts(): Flow<List<Product>> = productDao.getPurchasedProducts()
    
    fun getProductById(productId: Long): Flow<Product?> = productDao.getProductByIdFlow(productId)
    
    suspend fun insertProduct(product: Product): Long = productDao.insertProduct(product)
    
    suspend fun updateProduct(product: Product) = productDao.updateProduct(product)
    
    suspend fun deleteProduct(product: Product) = productDao.deleteProduct(product)
    
    suspend fun markAsPurchased(productId: Long) = productDao.markAsPurchased(productId)
    
    fun getActiveProductCount(): Flow<Int> = productDao.getActiveProductCount()
    
    fun getTotalWishlistValue(): Flow<Double> = productDao.getTotalWishlistValue().map { it ?: 0.0 }

    // Products with progress calculation
    fun getProductsWithProgress(): Flow<List<ProductWithProgress>> {
        return combine(
            productDao.getAllProducts(),
            budgetDao.getBudget()
        ) { products, budget ->
            val monthlySaving = budget?.monthlySaving ?: 0.0
            products.map { product ->
                calculateProgress(product, monthlySaving)
            }
        }
    }

    private fun calculateProgress(product: Product, monthlySaving: Double): ProductWithProgress {
        val monthsNeeded = if (monthlySaving > 0) {
            ceil(product.price / monthlySaving).toInt()
        } else {
            0
        }

        val progressPercentage = if (monthlySaving > 0 && monthsNeeded > 0) {
            // Calculate progress based on time passed since creation
            val monthsPassed = ((System.currentTimeMillis() - product.createdAt) / (30L * 24 * 60 * 60 * 1000)).toInt()
            ((monthsPassed.toFloat() / monthsNeeded) * 100f).coerceIn(0f, 100f)
        } else {
            0f
        }

        val estimatedDate = if (monthsNeeded > 0) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = product.createdAt
            calendar.add(Calendar.MONTH, monthsNeeded)
            SimpleDateFormat("MMM yyyy", Locale("ar")).format(calendar.time)
        } else {
            "غير محدد"
        }

        return ProductWithProgress(
            product,
            monthsNeeded,
            progressPercentage,
            estimatedDate
        )
    }

    // Budget
    fun getBudget(): Flow<Budget?> = budgetDao.getBudget()
    
    suspend fun getBudgetOnce(): Budget? = budgetDao.getBudgetOnce()
    
    suspend fun insertBudget(budget: Budget) = budgetDao.insertBudget(budget)
    
    suspend fun updateBudget(budget: Budget) = budgetDao.updateBudget(budget)

    // Offers
    fun getAllActiveOffers(): Flow<List<Offer>> = offerDao.getAllActiveOffers()
    
    suspend fun insertOffer(offer: Offer): Long = offerDao.insertOffer(offer)
    
    suspend fun insertOffers(offers: List<Offer>) = offerDao.insertOffers(offers)
    
    suspend fun deleteOffer(offer: Offer) = offerDao.deleteOffer(offer)
    
    suspend fun deactivateExpiredOffers() = offerDao.deactivateExpiredOffers()
}



