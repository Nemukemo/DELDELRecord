package com.example.deldelrecord.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.deldelrecord.data.Expense
import com.example.deldelrecord.data.ExpenseDao
import com.example.deldelrecord.data.ExpenseDatabase
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: ExpenseDao = ExpenseDatabase.getDatabase(application).expenseDao()

    // 全件取得を LiveData で変換して観察
    val allExpenses: LiveData<List<Expense>> = dao.getAllExpenses().asLiveData()

    private val _filteredExpenses = MutableLiveData<List<Expense>>()
    val filteredExpenses: LiveData<List<Expense>> = _filteredExpenses

    fun insertExpense(expense: Expense) {
        viewModelScope.launch {
            dao.insertExpense(expense) // ← repositoryではなくdaoを直接呼ぶ
        }
    }


    fun getExpensesByAmountRange(min: Int, max: Int) {
        viewModelScope.launch {
            _filteredExpenses.value = dao.getExpensesByAmountRange(min, max)
        }
    }

    fun getExpensesByTypes(types: List<String>) {
        viewModelScope.launch {
            _filteredExpenses.value = dao.getExpensesByTypes(types)
        }
    }

    fun getExpensesByDate(date: String) {
        viewModelScope.launch {
            _filteredExpenses.value = dao.getExpensesByDate(date)
        }
    }

    fun getExpensesByPartialDate(partial: String) {
        viewModelScope.launch {
            _filteredExpenses.value = dao.getExpensesByPartialDate(partial)
        }
    }

    fun getExpensesByDateRange(start: String, end: String) {
        viewModelScope.launch {
            _filteredExpenses.value = dao.getExpensesByDateRange(start, end)
        }
    }
}
