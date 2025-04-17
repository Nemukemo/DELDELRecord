package com.example.deldelrecord.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.deldelrecord.data.Expense
import com.example.deldelrecord.viewmodel.ExpenseViewModel

@Composable
fun ExpenseListScreen(
    navController: NavController,
    viewModel: ExpenseViewModel = viewModel()
) {
    //LiveDataの状態を監視し、全出費データと、フィルター後のデータを取得
    val allExpenses by viewModel.allExpenses.observeAsState(emptyList())
    val filteredExpenses by viewModel.filteredExpenses.observeAsState()

    //フィルターされた出費アがあればそれを使用し、なければ全出費データを使用
    val expenses = filteredExpenses ?: allExpenses

    //各種ダイアログの表示状態をrememberで管理
    var showFilterDialog by remember { mutableStateOf(false) }
    var showAmountDialog by remember { mutableStateOf(false) }
    var showTypeDialog by remember { mutableStateOf(false) }
    var showDateDialog by remember { mutableStateOf(false) }
    var showDateRangeDialog by remember { mutableStateOf(false) }

    /*レイアウトゾーン*/
    //メインの横並びレイアウト
    Column(modifier = Modifier.fillMaxSize()) {
        //上部の合計金額表示と表示絞り込みボタン
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            //出費合計金額を計算して表示する
            val totalAmount = expenses.sumOf { it.amount }
            Text("出費合計: ¥$totalAmount")

            // 絞り込みボタン（フィルター用ダイアログを表示）

            Button(
                onClick = { showFilterDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.height(48.dp)
            ) {
                Icon(Icons.Default.FilterList, contentDescription = "フィルター", tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("絞り込み", color = Color.White)
            }
        }

        //出費一覧を表示(LazyColumでリスト表示)
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(expenses) { expense ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
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

    //フィルターオプションのメインダイアログ
    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("絞り込み", color = Color.Black) },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    //金額フィルターへの遷移
                    FilterCardOption("金額の上限／下限") {
                        showAmountDialog = true
                        showFilterDialog = false
                    }
                    //出費種類フィルターへの遷移
                    FilterCardOption("出費の種類") {
                        showTypeDialog = true
                        showFilterDialog = false
                    }
                    //日付フィルターへの遷移
                    FilterCardOption("日付") {
                        showDateDialog = true
                        showFilterDialog = false
                    }
                    //日付範囲フィルターへの遷移
                    FilterCardOption("日付範囲") {
                        showDateRangeDialog = true
                        showFilterDialog = false
                    }
                }
            },
            confirmButton = {},
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
                        viewModel.getExpensesByAmountRange(lower, upper)
                        showAmountDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("適用", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAmountDialog = false }) {
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
                        viewModel.getExpensesByTypes(selected)
                        showTypeDialog = false
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
                        viewModel.getExpensesByPartialDate(partial)
                        showDateDialog = false
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
                        viewModel.getExpensesByDateRange(from, to)
                        showDateRangeDialog = false
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
        Box(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

//ToDo: 絞り込みのボタンの日付フィルター関連のボタンをDEMOであったカレンダーから選ぶデザインに変える
//TODO；合計金額を中央上(ヘッダー下)に表示する
//TODO：絞り込みボタンをFABにする
//ToDO：ダイアログ表示方法をボタン押して遷移ではなく全てダイアログ上に表示されるようにする
//TODO：日付関連をカレンダーに変更する(なのでカレンダーだけはボタン残しておく形にするもしくは一番上に表示する)
//TODO：ダイアログ内にリセットボタンの追加
