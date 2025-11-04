package com.example.app3.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.app3.data.model.Budget
import com.example.app3.data.model.Offer
import com.example.app3.data.model.Product

@Database(
    entities = [Product::class, Budget::class, Offer::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WishListDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun budgetDao(): BudgetDao
    abstract fun offerDao(): OfferDao

    companion object {
        @Volatile
        private var INSTANCE: WishListDatabase? = null

        fun getDatabase(context: Context): WishListDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WishListDatabase::class.java,
                    "wishlist_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


