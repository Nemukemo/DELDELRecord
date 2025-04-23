package com.example.deldelrecord.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.deldelrecord.viewmodel.ExpenseViewModel
import com.example.deldelrecord.viewmodel.FilterType

@Composable
fun ExpenseListScreen(
    navController: NavController,
    viewModel: ExpenseViewModel = viewModel()
) {
    val allExpenses by viewModel.allExpenses.observeAsState(emptyList())
    val filteredExpenses by viewModel.filteredExpenses.observeAsState()
    val expenses = filteredExpenses ?: allExpenses

    //各種ダイアログの表示状態をrememberで管理
    var showFilterDialog by remember { mutableStateOf(false) }
    var showAmountDialog by remember { mutableStateOf(false) }
    var showTypeDialog by remember { mutableStateOf(false) }
    var showDateDialog by remember { mutableStateOf(false) }
    var showDateRangeDialog by remember { mutableStateOf(false) }

    val totalAmount = expenses.sumOf { it.amount }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showFilterDialog = (true) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.FilterList, contentDescription = "フィルター")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ラベル
            Text(
                text = "合計金額",
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 8.dp),
                style = MaterialTheme.typography.titleMedium
            )

            // 金額表示
            Text(
                text = "¥${String.format("%,d", totalAmount)}円",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            )

            // 出費リスト
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 16.dp)
            ) {
                itemsIndexed(expenses) { index, expense ->
                    val isLast = index == expenses.lastIndex
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 4.dp,
                                bottom = if (isLast) 0.dp else 4.dp // 最後の項目だけ bottom 0
                            ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("種類: ${expense.type}")
                            Text("金額: ¥${expense.amount}")
                            Text("日付: ${expense.date}")
                        }
                    }
                }
            }

        }
    }


    // Filter dialog
    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("絞り込み", color = Color.Black)
                    IconButton(onClick = { showFilterDialog = false }) {
                        Icon(Icons.Default.Close, contentDescription = "閉じる")
                    }
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    FilterCardOption("金額の上限／下限") {
                        showFilterDialog = false
                        showAmountDialog = true
                    }
                    FilterCardOption("出費の種類") {
                        showFilterDialog = false
                        showTypeDialog = true
                    }
                    FilterCardOption("日付") {
                        showFilterDialog = false
                        showDateDialog = true
                    }
                    FilterCardOption("日付範囲") {
                        showFilterDialog = false
                        showDateRangeDialog = true
                    }
                }

            },
            confirmButton = {
                Button(
                    onClick = {
                        showFilterDialog = false
                        viewModel.applyFilter()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("適用", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("閉じる", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    //金額フィルターのダイアログ
    if (showAmountDialog) {
        var min by remember { mutableStateOf("") }
        var max by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAmountDialog = false },
            title = { Text("金額フィルター") },
            text = {
                Column {
                    //下限の金額入力
                    OutlinedTextField(
                        value = min,
                        onValueChange = { min = it },
                        label = { Text("下限") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    //上限の金額入力
                    OutlinedTextField(
                        value = max,
                        onValueChange = { max = it },
                        label = { Text("上限") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            },
            confirmButton = {
                //適用ボタン(ViewModelのメソッドを呼び出す)
                Button(
                    onClick = {
                        val lower = min.toIntOrNull() ?: 0
                        val upper = max.toIntOrNull() ?: Int.MAX_VALUE
                        viewModel.updateFilterCondition(
                            type = FilterType.MIN_AMOUNT,
                            value = lower
                        )
                        viewModel.updateFilterCondition(
                            type = FilterType.MAX_AMOUNT,
                            value = upper
                        )
                        showAmountDialog = false
                        showFilterDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("適用", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                    showAmountDialog = false
                    showFilterDialog = true
                }) {
                    Text("キャンセル", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    //出費種類フィルターのダイアログ
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
                Button(
                    onClick = {
                        viewModel.updateFilterCondition(
                            type=FilterType.TYPES,
                            value = selected.toList()
                        )
                        showTypeDialog = false
                        showFilterDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("適用", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTypeDialog = false }) {
                    Text("キャンセル", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    // 単一日付によるフィルター用ダイアログ
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
                Button(
                    onClick = {
                        val y = year.padStart(4, '0')
                        val m = month.padStart(2, '0')
                        val d = day.padStart(2, '0')
                        val partial = listOfNotNull(
                            if (year.isNotBlank()) y else null,
                            if (month.isNotBlank()) m else null,
                            if (day.isNotBlank()) d else null
                        ).joinToString("-")
                        viewModel.updateFilterCondition(
                            type = FilterType.DATE_TO,
                            value = partial
                        )
                        showDateDialog = false
                        showFilterDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("適用", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDateDialog = false }) {
                    Text("キャンセル", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    // 日付範囲によるフィルター用ダイアログ
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
                Button(
                    onClick = {
                        val from = "${fromYear.padStart(4, '0')}-${fromMonth.padStart(2, '0')}-${fromDay.padStart(2, '0')}"
                        val to = "${toYear.padStart(4, '0')}-${toMonth.padStart(2, '0')}-${toDay.padStart(2, '0')}"
                        viewModel.updateFilterCondition(
                            type = FilterType.DATE_TO,
                            value = from
                        )
                        viewModel.updateFilterCondition(
                            type = FilterType.DATE_FROM,
                            value = to
                        )
                        showDateRangeDialog = false
                        showFilterDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("適用", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDateRangeDialog = false }) {
                    Text("キャンセル", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }
}

// ダイアログ内の各オプション表示用カード（クリック可能）
@Composable
fun FilterCardOption(label: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = false, // Replace with a state variable to track the checkbox state
                onCheckedChange = { /* Handle checkbox state change */ }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

//ToDo: 絞り込みのボタンの日付フィルター関連のボタンをDEMOであったカレンダーから選ぶデザインに変える
//ToDO：ダイアログ表示方法をボタン押して遷移ではなく全てダイアログ上に表示されるようにする
//TODO：日付関連をカレンダーに変更する(なのでカレンダーだけはボタン残しておく形にするもしくは一番上に表示する)
//TODO：ダイアログ内にリセットボタンの追加

//TODO:LazyColumnとNavBottomとの間にある余白をつぶしたい(優先度低め)

