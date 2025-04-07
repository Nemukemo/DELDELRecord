package com.example.deldelrecord.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.asLiveData
import com.example.deldelrecord.data.Expense
import com.example.deldelrecord.data.ExpenseDatabase
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = ExpenseDatabase.getDatabase(application).expenseDao()

    val allExpenses: LiveData<List<Expense>> = dao.getAllExpenses().asLiveData()

    fun insertExpense(expense: Expense) {
        viewModelScope.launch {
            dao.insertExpense(expense)
        }
    }

    fun getExpensesByAmountRange(minAmount: Int, maxAmount: Int): LiveData<List<Expense>> {
        val result = MutableLiveData<List<Expense>>()
        viewModelScope.launch {
            result.postValue(dao.getExpensesByAmountRange(minAmount, maxAmount))
        }
        return result
    }

    fun getExpensesByType(expenseType: String): LiveData<List<Expense>> {
        val result = MutableLiveData<List<Expense>>()
        viewModelScope.launch {
            result.postValue(dao.getExpensesByType(expenseType))
        }
        return result
    }

    fun getExpensesByTypes(expenseTypes: List<String>): LiveData<List<Expense>> {
        val result = MutableLiveData<List<Expense>>()
        viewModelScope.launch {
            result.postValue(dao.getExpensesByTypes(expenseTypes))
        }
        return result
    }


    fun getExpensesByDate(date: String): LiveData<List<Expense>> {
        val result = MutableLiveData<List<Expense>>()
        viewModelScope.launch {
            result.postValue(dao.getExpensesByDate(date))
        }
        return result
    }

    fun getExpensesByDateRange(startDate: String, endDate: String): LiveData<List<Expense>> {
        val result = MutableLiveData<List<Expense>>()
        viewModelScope.launch {
            result.postValue(dao.getExpensesByDateRange(startDate, endDate))
        }
        return result
    }
}