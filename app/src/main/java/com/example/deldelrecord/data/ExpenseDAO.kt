package com.example.deldelrecord.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    // 通常の全件取得を Flow で返す（UI更新に強い）
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    // 金額範囲で取得
    @Query("SELECT * FROM expenses WHERE amount BETWEEN :minAmount AND :maxAmount ORDER BY amount ASC")
    suspend fun getExpensesByAmountRange(minAmount: Int, maxAmount: Int): List<Expense>

    // 種類（複数）で取得
    @Query("SELECT * FROM expenses WHERE type IN (:expenseTypes) ORDER BY date DESC")
    suspend fun getExpensesByTypes(expenseTypes: List<String>): List<Expense>

    // 日付で完全一致取得
    @Query("SELECT * FROM expenses WHERE date = :date ORDER BY amount ASC")
    suspend fun getExpensesByDate(date: String): List<Expense>

    // 部分一致（日・月など）取得
    @Query("SELECT * FROM expenses WHERE date LIKE :partialDate || '%' ORDER BY date ASC")
    suspend fun getExpensesByPartialDate(partialDate: String): List<Expense>

    // 日付範囲で取得
    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    suspend fun getExpensesByDateRange(startDate: String, endDate: String): List<Expense>
}
