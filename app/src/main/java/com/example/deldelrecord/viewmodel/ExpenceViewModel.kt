package com.example.deldelrecord.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.deldelrecord.data.Expense
import com.example.deldelrecord.data.ExpenseDatabase
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = ExpenseDatabase.getDatabase(application).expenseDao()

    private val _expenses = MutableLiveData<List<Expense>>()
    val expenses: LiveData<List<Expense>> = _expenses

    val allExpenses: LiveData<List<Expense>> = dao.getAllExpenses().asLiveData()

    fun insertExpense(expense: Expense) {
        viewModelScope.launch {
            dao.insertExpense(expense)
            refreshAllExpenses() // 追加後にリスト更新
        }
    }

    fun refreshAllExpenses() {
        viewModelScope.launch {
            _expenses.postValue(dao.getAllExpensesDirect()) // suspend版を別途用意
        }
    }

    fun filterByAmount(minAmount: Int, maxAmount: Int) {
        viewModelScope.launch {
            _expenses.postValue(dao.getExpensesByAmountRange(minAmount, maxAmount))
        }
    }

    fun filterByTypes(expenseTypes: List<String>) {
        viewModelScope.launch {
            _expenses.postValue(dao.getExpensesByTypes(expenseTypes))
        }
    }

    fun filterByDate(date: String) {
        viewModelScope.launch {
            _expenses.postValue(dao.getExpensesByDate(date))
        }
    }

    fun filterByPartialDate(partialDate: String) {
        viewModelScope.launch {
            _expenses.postValue(dao.getExpensesByPartialDate(partialDate))
        }
    }

    fun filterByDateRange(startDate: String, endDate: String) {
        viewModelScope.launch {
            _expenses.postValue(dao.getExpensesByDateRange(startDate, endDate))
        }
    }
}
