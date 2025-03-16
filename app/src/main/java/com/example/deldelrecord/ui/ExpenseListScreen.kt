package com.example.deldelrecord.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.deldelrecord.data.Expense
import com.example.deldelrecord.viewmodel.ExpenseViewModel

@Composable
fun ExpenseListScreen(navController: NavController, viewModel: ExpenseViewModel = viewModel()) {
    val expenses by viewModel.allExpenses.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("記録一覧") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("合計金額: ${expenses.sumOf { it.amount }} 円", style = MaterialTheme.typography.h6)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* 絞り込み機能追加予定 */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("絞り込み")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(expenses) { expense ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("金額: ${expense.amount} 円", style = MaterialTheme.typography.subtitle1)
                            Text("種類: ${expense.type}", style = MaterialTheme.typography.body1)
                            Text("日付: ${expense.date}", style = MaterialTheme.typography.body2)
                            expense.memo?.let {
                                Text("メモ: $it", style = MaterialTheme.typography.body2)
                            }
                        }
                    }
                }
            }
        }
    }
}
