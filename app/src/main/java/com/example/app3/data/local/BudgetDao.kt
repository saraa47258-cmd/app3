package com.example.app3.data.local

import androidx.room.*
import com.example.app3.data.model.Budget
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budget WHERE id = 1")
    fun getBudget(): Flow<Budget?>

    @Query("SELECT * FROM budget WHERE id = 1")
    suspend fun getBudgetOnce(): Budget?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget)

    @Update
    suspend fun updateBudget(budget: Budget)

    @Query("DELETE FROM budget")
    suspend fun deleteBudget()
}


