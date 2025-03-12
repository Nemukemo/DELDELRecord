// ExpenseInputScreen.kt - Case 3: インタラクティブ＆ゲーム要素の導入 (Rich Modern Design)
package com.example.deldelrecord.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.*

@Composable
fun ExpenseInputScreen(navController: NavController) {
    var amount by remember { mutableStateOf("") }
    var memo by remember { mutableStateOf("") }
    val expenseTypes = listOf("修学費", "食費", "娯楽費")
    var selectedType by remember { mutableStateOf(expenseTypes.first()) }

    // APIレベル24対応のため Calendar を利用
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val years = (currentYear downTo currentYear - 10).toList()
    val months = (1..12).toList()
    val days = (1..31).toList()

    var selectedYear by remember { mutableStateOf(currentYear) }
    var selectedMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH) + 1) }
    var selectedDay by remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }

    var yearExpanded by remember { mutableStateOf(false) }
    var monthExpanded by remember { mutableStateOf(false) }
    var dayExpanded by remember { mutableStateOf(false) }

    var confirmPressed by remember { mutableStateOf(false) }
    val buttonColor by animateColorAsState(
        targetValue = if (confirmPressed) Color(0xFF4CAF50) else MaterialTheme.colors.primary,
        animationSpec = androidx.compose.animation.core.tween(500)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("出費入力", style = typography.h6) },
                backgroundColor = MaterialTheme.colors.primary,
                elevation = 8.dp
            )
        },
        backgroundColor = MaterialTheme.colors.background
    ) { padding ->
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            backgroundColor = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                Text("出費の種類", style = typography.subtitle1)
                expenseTypes.forEach { type ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = (selectedType == type),
                            onClick = { selectedType = type },
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary)
                        )
                        Text(text = type, style = typography.body1)
                    }
                }
                Text("日付を選択", style = typography.subtitle1)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 年の選択
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = selectedYear.toString(),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("年") },
                            trailingIcon = {
                                IconButton(onClick = { yearExpanded = !yearExpanded }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        DropdownMenu(
                            expanded = yearExpanded,
                            onDismissRequest = { yearExpanded = false }
                        ) {
                            years.forEach { year ->
                                DropdownMenuItem(onClick = {
                                    selectedYear = year
                                    yearExpanded = false
                                }) {
                                    Text(year.toString())
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    // 月の選択
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = selectedMonth.toString(),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("月") },
                            trailingIcon = {
                                IconButton(onClick = { monthExpanded = !monthExpanded }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        DropdownMenu(
                            expanded = monthExpanded,
                            onDismissRequest = { monthExpanded = false }
                        ) {
                            months.forEach { month ->
                                DropdownMenuItem(onClick = {
                                    selectedMonth = month
                                    monthExpanded = false
                                }) {
                                    Text(month.toString())
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    // 日の選択
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = selectedDay.toString(),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("日") },
                            trailingIcon = {
                                IconButton(onClick = { dayExpanded = !dayExpanded }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        DropdownMenu(
                            expanded = dayExpanded,
                            onDismissRequest = { dayExpanded = false }
                        ) {
                            days.forEach { day ->
                                DropdownMenuItem(onClick = {
                                    selectedDay = day
                                    dayExpanded = false
                                }) {
                                    Text(day.toString())
                                }
                            }
                        }
                    }
                }
                OutlinedTextField(
                    value = memo,
                    onValueChange = { memo = it },
                    label = { Text("メモ (任意)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        confirmPressed = true
                        // ここに保存処理などを追加
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("確定", style = typography.button)
                }
            }
        }
    }
}
