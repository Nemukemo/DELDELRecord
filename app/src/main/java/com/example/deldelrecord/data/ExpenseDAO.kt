package com.example.deldelrecord.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>> // 🔹 LiveData の代わりに Flow を使用

    // ① 金額の上限、下限を設定してソート（例：金額昇順）
    @Query("SELECT * FROM expenses WHERE amount BETWEEN :minAmount AND :maxAmount ORDER BY amount ASC")
    fun getExpensesByAmountRange(minAmount: Int, maxAmount: Int): List<Expense>

    // ② 出費の種類を選択してソート（例：日付降順）
    @Query("SELECT * FROM expenses WHERE type = :expenseType ORDER BY date DESC")
    fun getExpensesByType(expenseType: String): List<Expense>

    @Query("SELECT * FROM expenses WHERE type IN (:expenseTypes) ORDER BY date DESC")
    fun getExpensesByTypes(expenseTypes: List<String>): List<Expense>


    // ③ 特定の日付を選択してソート（例：金額昇順）
    @Query("SELECT * FROM expenses WHERE date = :date ORDER BY amount ASC")
    fun getExpensesByDate(date: String): List<Expense>

    // ④ 日付範囲を選択してソート（例：日付昇順）
    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getExpensesByDateRange(startDate: String, endDate: String): List<Expense>

}
