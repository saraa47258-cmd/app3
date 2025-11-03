package com.example.app3.data.local

import androidx.room.*
import com.example.app3.data.model.Offer
import kotlinx.coroutines.flow.Flow

@Dao
interface OfferDao {
    @Query("SELECT * FROM offers WHERE isActive = 1 ORDER BY discountPercentage DESC")
    fun getAllActiveOffers(): Flow<List<Offer>>

    @Query("SELECT * FROM offers WHERE id = :offerId")
    suspend fun getOfferById(offerId: Long): Offer?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOffer(offer: Offer): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOffers(offers: List<Offer>)

    @Update
    suspend fun updateOffer(offer: Offer)

    @Delete
    suspend fun deleteOffer(offer: Offer)

    @Query("DELETE FROM offers WHERE id = :offerId")
    suspend fun deleteOfferById(offerId: Long)

    @Query("DELETE FROM offers WHERE expiryDate < :currentTime")
    suspend fun deleteExpiredOffers(currentTime: Long = System.currentTimeMillis())

    @Query("UPDATE offers SET isActive = 0 WHERE expiryDate < :currentTime")
    suspend fun deactivateExpiredOffers(currentTime: Long = System.currentTimeMillis())
}


