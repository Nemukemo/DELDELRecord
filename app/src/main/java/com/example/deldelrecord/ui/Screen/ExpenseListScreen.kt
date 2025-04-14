package com.example.deldelrecord.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.deldelrecord.data.Expense
import com.example.deldelrecord.viewmodel.ExpenseViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults


@Composable
fun ExpenseListScreen(
    navController: NavController,
    viewModel: ExpenseViewModel = viewModel()
) {
    val allExpenses by viewModel.allExpenses.observeAsState(emptyList())
    val filteredExpenses by viewModel.filteredExpenses.observeAsState()

    val expenses = filteredExpenses ?: allExpenses

    // ダイアログ状態
    var showFilterDialog by remember { mutableStateOf(false) }
    var showAmountDialog by remember { mutableStateOf(false) }
    var showTypeDialog by remember { mutableStateOf(false) }
    var showDateDialog by remember { mutableStateOf(false) }
    var showDateRangeDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            val totalAmount = expenses.sumOf { it.amount }
            Text("Total Amount: ¥$totalAmount")
            Button(onClick = { showFilterDialog = true }) {
                Text("絞り込み")
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(expenses) { expense ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("種類: ${expense.type}")
                        Text("金額: ¥${expense.amount}")
                        Text("日付: ${expense.date}")
                    }
                }
            }
        }
    }

    // ---- 以下はすべて絞り込みダイアログたち ----

    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("絞り込み") },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    FilterCardOption("金額の上限／下限") {
                        showAmountDialog = true
                        showFilterDialog = false
                    }
                    FilterCardOption("出費の種類") {
                        showTypeDialog = true
                        showFilterDialog = false
                    }
                    FilterCardOption("日付") {
                        showDateDialog = true
                        showFilterDialog = false
                    }
                    FilterCardOption("日付範囲") {
                        showDateRangeDialog = true
                        showFilterDialog = false
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                Button(onClick = { showFilterDialog = false }) {
                    Text("閉じる")
                }
            }
        )
    }



    if (showAmountDialog) {
        var min by remember { mutableStateOf("") }
        var max by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAmountDialog = false },
            title = { Text("金額フィルター") },
            text = {
                Column {
                    OutlinedTextField(
                        value = min,
                        onValueChange = { min = it },
                        label = { Text("下限") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = max,
                        onValueChange = { max = it },
                        label = { Text("上限") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val lower = min.toIntOrNull() ?: 0
                    val upper = max.toIntOrNull() ?: Int.MAX_VALUE
                    viewModel.getExpensesByAmountRange(lower, upper)
                    showAmountDialog = false
                }) {
                    Text("適用")
                }
            },
            dismissButton = {
                Button(onClick = { showAmountDialog = false }) {
                    Text("キャンセル")
                }
            }
        )
    }

    if (showTypeDialog) {
        val types = listOf("修学費", "食費", "娯楽費")
        val selected = remember { mutableStateListOf<String>() }

        AlertDialog(
            onDismissRequest = { showTypeDialog = false },
            title = { Text("種類フィルター") },
            text = {
                Column {
                    types.forEach { type ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .toggleable(
                                    value = selected.contains(type),
                                    onValueChange = {
                                        if (it) selected.add(type) else selected.remove(type)
                                    }
                                )
                        ) {
                            Checkbox(checked = selected.contains(type), onCheckedChange = null)
                            Text(type, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.getExpensesByTypes(selected)
                    showTypeDialog = false
                }) {
                    Text("適用")
                }
            },
            dismissButton = {
                Button(onClick = { showTypeDialog = false }) {
                    Text("キャンセル")
                }
            }
        )
    }

    if (showDateDialog) {
        var year by remember { mutableStateOf("") }
        var month by remember { mutableStateOf("") }
        var day by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showDateDialog = false },
            title = { Text("日付フィルター") },
            text = {
                Column {
                    OutlinedTextField(value = year, onValueChange = { year = it }, label = { Text("年") })
                    OutlinedTextField(value = month, onValueChange = { month = it }, label = { Text("月") })
                    OutlinedTextField(value = day, onValueChange = { day = it }, label = { Text("日") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    val y = year.padStart(4, '0')
                    val m = month.padStart(2, '0')
                    val d = day.padStart(2, '0')
                    val partial = listOfNotNull(
                        if (year.isNotBlank()) y else null,
                        if (month.isNotBlank()) m else null,
                        if (day.isNotBlank()) d else null
                    ).joinToString("-")
                    viewModel.getExpensesByPartialDate(partial)
                    showDateDialog = false
                }) {
                    Text("適用")
                }
            },
            dismissButton = {
                Button(onClick = { showDateDialog = false }) {
                    Text("キャンセル")
                }
            }
        )
    }

    if (showDateRangeDialog) {
        var fromYear by remember { mutableStateOf("") }
        var fromMonth by remember { mutableStateOf("") }
        var fromDay by remember { mutableStateOf("") }

        var toYear by remember { mutableStateOf("") }
        var toMonth by remember { mutableStateOf("") }
        var toDay by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showDateRangeDialog = false },
            title = { Text("日付範囲フィルター") },
            text = {
                Column {
                    Text("開始日")
                    OutlinedTextField(value = fromYear, onValueChange = { fromYear = it }, label = { Text("年") })
                    OutlinedTextField(value = fromMonth, onValueChange = { fromMonth = it }, label = { Text("月") })
                    OutlinedTextField(value = fromDay, onValueChange = { fromDay = it }, label = { Text("日") })

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("終了日")
                    OutlinedTextField(value = toYear, onValueChange = { toYear = it }, label = { Text("年") })
                    OutlinedTextField(value = toMonth, onValueChange = { toMonth = it }, label = { Text("月") })
                    OutlinedTextField(value = toDay, onValueChange = { toDay = it }, label = { Text("日") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    val from = "${fromYear.padStart(4, '0')}-${fromMonth.padStart(2, '0')}-${fromDay.padStart(2, '0')}"
                    val to = "${toYear.padStart(4, '0')}-${toMonth.padStart(2, '0')}-${toDay.padStart(2, '0')}"
                    viewModel.getExpensesByDateRange(from, to)
                    showDateRangeDialog = false
                }) {
                    Text("適用")
                }
            },
            dismissButton = {
                Button(onClick = { showDateRangeDialog = false }) {
                    Text("キャンセル")
                }
            }
        )
    }
}

// 共通で使うカード風のオプションコンポーネント
@Composable
fun FilterCardOption(label: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        elevation = 6.dp
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

