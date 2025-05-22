package com.example.deldelrecord.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.deldelrecord.data.Expense
import com.example.deldelrecord.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
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

    val calendar = Calendar.getInstance()
    var selectedDateMillis by remember { mutableStateOf(calendar.timeInMillis) }
    var showDateSelectDialog by remember { mutableStateOf(false) }

    val formattedDate = remember(selectedDateMillis) {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(selectedDateMillis))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("支出入力", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            OutlinedTextField(
                value = amount,
                onValueChange = { input ->
                    amount = input.filter { it.isDigit() }
                },
                label = { Text("金額") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Text("出費の種類", style = MaterialTheme.typography.titleMedium)
            expenseTypes.forEach { type ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (selectedType == type),
                            onClick = { selectedType = type }
                        )
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = (selectedType == type),
                        onClick = { selectedType = type }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = type)
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
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
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = {
                    val expenseAmount = amount.toIntOrNull()
                    if (expenseAmount != null && expenseAmount > 0) {
                        val expense = Expense(
                            amount = expenseAmount,
                            type = selectedType,
                            date = formattedDate,
                            memo = memo.ifBlank { null }
                        )
                        viewModel.insertExpense(expense)
                        amount = ""
                        memo = ""
                        selectedType = expenseTypes.first()
                        selectedDateMillis = Calendar.getInstance().timeInMillis
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("確定", color = Color.White)
            }
        }
    }

    if (showDateSelectDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDateMillis
        )

        val onDateSelected: (Long?) -> Unit = { millis ->
            millis?.let {
                selectedDateMillis = it
            }
        }

        DatePickerDialog(
            onDismissRequest = { showDateSelectDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDateSelected(datePickerState.selectedDateMillis)
                        showDateSelectDialog = false
                    }
                ) {
                    Text("適用")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDateSelectDialog = false }) {
                    Text("キャンセル")
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
