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
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
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
                            date = "$selectedYear-${"%02d".format(selectedMonth)}-${"%02d".format(selectedDay)}",
                            memo = memo.ifBlank { null }
                        )
                        viewModel.insertExpense(expense)
                        amount = ""
                        memo = ""
                        selectedType = expenseTypes.first()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("確定", color = Color.White)
            }

            TextButton(
                onClick = { navController.navigate("ExpenseInput_calendar") },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("日付を入力したいあなたへ", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
