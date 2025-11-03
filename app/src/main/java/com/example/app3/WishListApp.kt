package com.example.app3

import android.app.Application
import com.example.app3.data.local.WishListDatabase
import com.example.app3.data.repository.WishListRepository

class WishListApp : Application() {
    private val database by lazy { WishListDatabase.getDatabase(this) }
    val repository by lazy {
        WishListRepository(
            database.productDao(),
            database.budgetDao(),
            database.offerDao()
        )
    }
}


