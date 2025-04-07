package com.example.deldelrecord.ui.screens

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
fun ExpenseListScreen(
    viewModel: ExpenseViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var showFilterDialog by remember { mutableStateOf(false) }

    // Example row with "Total Amount" text and filter button
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text("Total Amount: 10000") // Just an example
        Button(onClick = { showFilterDialog = true }) {
            Text("絞り込み")
        }
    }

    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("絞り込み") },
            text = {
                Column {
                    // 1) Amount range filter
                    Button(onClick = {
                        viewModel.getExpensesByAmountRange(0, 1000)
                        showFilterDialog = false
                    }) {
                        Text("金額の上限／下限")
                    }
                    // 2) Type filter
                    Button(onClick = {
                        viewModel.getExpensesByType("食費")
                        showFilterDialog = false
                    }) {
                        Text("出費の種類")
                    }
                    // 3) Date filter
                    Button(onClick = {
                        viewModel.getExpensesByDate("2024-01-01")
                        showFilterDialog = false
                    }) {
                        Text("日付")
                    }
                    // 4) Date range filter
                    Button(onClick = {
                        viewModel.getExpensesByDateRange("2024-01-01", "2024-01-31")
                        showFilterDialog = false
                    }) {
                        Text("日付範囲")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                Button(onClick = { showFilterDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}