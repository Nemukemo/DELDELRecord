@Composable
fun ExpenseListScreen(
    navController: NavController,
    viewModel: ExpenseViewModel = viewModel()
) {
    var showFilterDialog by remember { mutableStateOf(false) }
    var showAmountDialog by remember { mutableStateOf(false) }
    var showTypeDialog by remember { mutableStateOf(false) }
    var showDateDialog by remember { mutableStateOf(false) }
    var showDateRangeDialog by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text("Total Amount: 10000") // 実データに変更可能
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
                    Button(onClick = {
                        showAmountDialog = true
                        showFilterDialog = false
                    }) {
                        Text("金額の上限／下限")
                    }
                    Button(onClick = {
                        showTypeDialog = true
                        showFilterDialog = false
                    }) {
                        Text("出費の種類")
                    }
                    Button(onClick = {
                        showDateDialog = true
                        showFilterDialog = false
                    }) {
                        Text("日付")
                    }
                    Button(onClick = {
                        showDateRangeDialog = true
                        showFilterDialog = false
                    }) {
                        Text("日付範囲")
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
