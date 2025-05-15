package com.example.deldelrecord.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.deldelrecord.data.Expense
import com.example.deldelrecord.viewmodel.ExpenseViewModel

import java.util.*

@Composable
fun ExpenseInputScreen(
    navController: NavController,
    viewModel: ExpenseViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var amount by remember { mutableStateOf("") }
    var memo by remember { mutableStateOf("") }
    val expenseTypes = listOf("修学費", "食費", "娯楽費")
    var selectedType by remember { mutableStateOf(expenseTypes.first()) }

    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    var selectedYear by remember { mutableStateOf(currentYear) }
    var selectedMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH) + 1) }
    var selectedDay by remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("手入力画面") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it.filter { char -> char.isDigit() } },
                label = { Text("金額") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Text("出費の種類")
            expenseTypes.forEach { type ->
                Row {
                    RadioButton(
                        selected = (selectedType == type),
                        onClick = { selectedType = type }
                    )
                    Text(text = type)
                }
            }
            OutlinedTextField(
                value = memo,
                onValueChange = { memo = it },
                label = { Text("メモ (任意)") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    val expense = Expense(
                        amount = amount.toIntOrNull() ?: 0,
                        type = selectedType,
                        date = "$selectedYear-${"%02d".format(selectedMonth)}-${"%02d".format(selectedDay)}",
                        memo = memo.ifBlank { null }
                    )
                    viewModel.insertExpense(expense) // ← viewModel() を再取得せず直接呼び出し

                    // 入力完了後の初期化（任意）
                    amount = ""
                    memo = ""
                    selectedType = expenseTypes.first()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("確定")
            }

            // ExpenceInput_calendar.ktへの遷移ボタン
            androidx.compose.material3.Button(
                onClick = {
                    navController.navigate("ExpenseInput_calendar")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                androidx.compose.material3.Text("日付を入力したいあなたへ", color = Color.White)
            }

        }
    }
}
//TODO: ExpenseListScreen.kt を参考にモダンなシンプルデザインにしてちょ
//TODO: 入力完了時に誤った場合に削除できるようにしたい。具体的な構想としてはtoastをタップした際に削除しますかのダイアログを出すとか