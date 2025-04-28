package com.example.deldelrecord.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.deldelrecord.data.Expense
import com.example.deldelrecord.data.ExpenseDao
import com.example.deldelrecord.data.ExpenseDatabase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter as DataTimeFormatter

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: ExpenseDao = ExpenseDatabase.getDatabase(application).expenseDao()

    // 全件取得を LiveData で変換して観察
    val allExpenses: LiveData<List<Expense>> = dao.getAllExpenses().asLiveData()

    // 取得したデータをフィルタリングするための LiveData
    private val _filteredExpenses = MutableLiveData<List<Expense>>()

    // フィルタリングされたデータを外部から観察できるようにする
    val filteredExpenses: LiveData<List<Expense>> = _filteredExpenses


    // フィルタリング条件を保持するための構造体
    var currentFilterCondition = FilterCondition()
        private set

    //DBに追加を行うメソッド
    fun insertExpense(expense: Expense) {
        viewModelScope.launch {
            dao.insertExpense(expense) // ← repositoryではなくdaoを直接呼ぶ
        }
    }

    //フィルタリングを適用するためのデータクラス
    fun updateFilterCondition(type: FilterType, value: Any?) {
        val formatter = DataTimeFormatter.ofPattern("yyyy-MM-dd")
        when (type) {
            FilterType.MIN_AMOUNT -> currentFilterCondition.minAmount = value as? Int
            FilterType.MAX_AMOUNT -> currentFilterCondition.maxAmount = value as? Int
            FilterType.TYPES      -> currentFilterCondition.types = value as? List<String>
            FilterType.DATE_SINGLE -> currentFilterCondition.dateSingle = (value as? String)?.let { LocalDate.parse(it,formatter) }
            FilterType.DATE_FROM       -> currentFilterCondition.dateFrom = (value as? String)?.let { LocalDate.parse(it,formatter) }
            FilterType.DATE_TO    -> currentFilterCondition.dateTo = (value as? String)?.let { LocalDate.parse(it,formatter) }
        }
    }

    // フィルタリング条件を適用してデータをフィルタリングするメソッド
    fun applyFilter() {
        val all = allExpenses.value ?: return
        _filteredExpenses.value = all.filter { expense ->
            val f = currentFilterCondition
            val localminAmount = f.minAmount ?: 0
            val localmaxAmount = f.maxAmount ?: 0
            val localType = f.types ?: emptyList()

            val amountOk = (f.minAmount == null || expense.amount >= localminAmount) &&
                    (f.maxAmount == null || expense.amount <= localmaxAmount)
            val typeOk = f.types.isNullOrEmpty() || localType.contains(expense.type)
            val dateOk = when {
                f.dateSingle != null -> expense.date == f.dateSingle.toString()
                f.dateFrom != null || f.dateTo != null -> {
                    (f.dateFrom == null || expense.date >= f.dateFrom.toString()) &&
                            (f.dateTo == null || expense.date <= f.dateTo.toString())
                }

                else -> true // どちらも指定なしなら通す
            }
            amountOk && typeOk && dateOk
        }
    }

    //フィルターリセット用
    fun resetFilter() {
        currentFilterCondition = FilterCondition()
        _filteredExpenses.value = allExpenses.value
    }

    //フィルターをしているかどうかのフラグ用
    fun isFiltered(): Boolean {
        val f = currentFilterCondition
        return f.minAmount != null || f.maxAmount != null || !f.types.isNullOrEmpty()
                || f.dateFrom != null || f.dateTo != null
    }
    fun setSingleDate(date: LocalDate?) {
        currentFilterCondition.dateSingle = date
        currentFilterCondition.dateFrom = null
        currentFilterCondition.dateTo = null
    }

    fun setDateRange(from: LocalDate?, to: LocalDate?) {
        currentFilterCondition.dateFrom = from
        currentFilterCondition.dateTo = to
        currentFilterCondition.dateSingle = null
    }

}
