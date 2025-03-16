package com.example.deldelrecord.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.asLiveData
import com.example.deldelrecord.data.Expense
import com.example.deldelrecord.data.ExpenseDatabase
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = ExpenseDatabase.getDatabase(application).expenseDao()

    // 🔹 Room のデータを監視
    val allExpenses: LiveData<List<Expense>> = dao.getAllExpenses().asLiveData()

    // 🔹 出費データを追加
    fun insertExpense(expense: Expense) {
        viewModelScope.launch {
            dao.insertExpense(expense)
        }
    }
}
