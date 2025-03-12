// ExpenseListScreen.kt - Case 3: インタラクティブ＆ゲーム要素の導入 (Rich Modern Design)
package com.example.deldelrecord.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class ExpenseRecord(
    val amount: Int,
    val type: String,
    val date: String,
    val memo: String?
)

@Composable
fun ExpenseListScreen(navController: NavController) {
    var expenseRecords by remember {
        mutableStateOf(
            listOf(
                ExpenseRecord(1000, "食費", "2025/03/05", "昼食"),
                ExpenseRecord(2000, "娯楽費", "2025/03/04", "映画"),
                ExpenseRecord(1500, "修学費", "2025/03/03", "参考書")
            )
        )
    }
    val totalAmount = expenseRecords.sumOf { it.amount }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("記録一覧", style = typography.h6) },
                backgroundColor = MaterialTheme.colors.primary,
                elevation = 8.dp
            )
        },
        backgroundColor = MaterialTheme.colors.background
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // ヘッダー部分：グラデーション背景で合計金額を表示
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(MaterialTheme.colors.primary, MaterialTheme.colors.secondary)
                        )
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("合計金額: $totalAmount 円", style = typography.h5, color = MaterialTheme.colors.onPrimary)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* 絞り込み機能追加予定 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("絞り込み", style = typography.button, color = MaterialTheme.colors.onSecondary)
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                itemsIndexed(expenseRecords) { index, record ->
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(index) {
                        kotlinx.coroutines.delay(100L * index)
                        visible = true
                    }
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = MaterialTheme.shapes.medium,
                            elevation = 4.dp,
                            backgroundColor = MaterialTheme.colors.surface
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("金額: ${record.amount} 円", style = typography.subtitle1)
                                Text("種類: ${record.type}", style = typography.body1)
                                Text("日付: ${record.date}", style = typography.body2)
                                record.memo?.takeIf { it.isNotBlank() }?.let {
                                    Text("メモ: $it", style = typography.body2)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
