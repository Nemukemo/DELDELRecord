package com.example.deldelrecord.ui.Screen

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.deldelrecord.data.Expense
import com.example.deldelrecord.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate

import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenceInput_calendar(
    navController: NavController,
    viewModel: ExpenseViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var amount by remember { mutableStateOf("") }
    var memo by remember { mutableStateOf("") }
    val expenseTypes = listOf("修学費", "食費", "娯楽費")
    var selectedType by remember { mutableStateOf(expenseTypes.first()) }


    // 初期日付
    val calendar = Calendar.getInstance()
    var selectedDateMillis by remember { mutableStateOf(calendar.timeInMillis) }
    var showDateSelectDialog by remember { mutableStateOf(false) }


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
            Button(onClick = {navController.popBackStack()}) {
                Text("戻る")
            }
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

            // 日付表示と選択
            val formattedDate = remember(selectedDateMillis) {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(selectedDateMillis))
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("選択日付: $formattedDate")
                TextButton(onClick = { showDateSelectDialog = true }) {
                    Text("日付を選ぶ")
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
                    val formattedDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .format(Date(selectedDateMillis))

                    val expense = Expense(
                        amount = amount.toIntOrNull() ?: 0,
                        type = selectedType,
                        date = formattedDateStr,
                        memo = memo.ifBlank { null }
                    )
                    viewModel.insertExpense(expense)

                    // 初期化
                    amount = ""
                    memo = ""
                    selectedType = expenseTypes.first()
                    selectedDateMillis = Calendar.getInstance().timeInMillis
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("確定")
            }
        }
    }

    if(showDateSelectDialog){
        //カレンダーを呼び出す
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis()
        )

        // 日付選択時の処理
        val onDateSelected: (Long?) -> Unit = { millis ->
            millis?.let {
                selectedDateMillis = it // Update selectedDateMillis with the chosen date
            }
            }


        DatePickerDialog(
            onDismissRequest = {showDateSelectDialog = false},
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        onDateSelected(datePickerState.selectedDateMillis) // Pass the selected date
                        showDateSelectDialog = false
                    }
                ) {
                    androidx.compose.material3.Text("適用")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = {
                    showDateSelectDialog = false
                }) {
                    androidx.compose.material3.Text("キャンセル")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false
            )
        }
    }
}

