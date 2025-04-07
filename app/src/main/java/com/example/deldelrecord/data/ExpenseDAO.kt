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

    @Query("SELECT * FROM expense ORDER BY date DESC")
    suspend fun getAllExpensesDirect(): List<Expense>


    // ① 金額の上限、下限を設定してソート（例：金額昇順）
    @Query("SELECT * FROM expenses WHERE amount BETWEEN :minAmount AND :maxAmount ORDER BY amount ASC")
    suspend fun getExpensesByAmountRange(minAmount: Int, maxAmount: Int): List<Expense>

    // ② 出費の種類を選択してソート（例：日付降順）
    @Query("SELECT * FROM expenses WHERE type = :expenseType ORDER BY date DESC")
    suspend fun getExpensesByType(expenseType: String): List<Expense>

    @Query("SELECT * FROM expenses WHERE type IN (:expenseTypes) ORDER BY date DESC")
    suspend fun getExpensesByTypes(expenseTypes: List<String>): List<Expense>


    // ③ 特定の日付を選択してソート（例：金額昇順）
    @Query("SELECT * FROM expenses WHERE date = :date ORDER BY amount ASC")
    suspend fun getExpensesByDate(date: String): List<Expense>

    // ④ 日付範囲を選択してソート（例：日付昇順）
    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    suspend fun getExpensesByDateRange(startDate: String, endDate: String): List<Expense>

    @Query("SELECT * FROM expenses WHERE date LIKE :partialDate || '%' ORDER BY date ASC")
    suspend fun getExpensesByPartialDate(partialDate: String): List<Expense>

}
