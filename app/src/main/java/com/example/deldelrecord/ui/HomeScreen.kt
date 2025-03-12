package com.example.deldelrecord.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    var showWelcome by remember { mutableStateOf(true) }

    // 1.5秒後にウェルカム表示を解除
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1500)
        showWelcome = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("デルデルレコード", style = typography.h6) },
                backgroundColor = MaterialTheme.colors.primary,
                elevation = 8.dp
            )
        },
        backgroundColor = MaterialTheme.colors.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(MaterialTheme.colors.secondaryVariant, MaterialTheme.colors.background)
                    )
                )
                .padding(padding),
            contentAlignment = Alignment.Center  // ここでボタン群を **縦横のど真ん中** に配置
        ) {
            Crossfade(
                targetState = showWelcome,
                animationSpec = tween(durationMillis = 1000)
            ) { showWelcomeState ->
                if (showWelcomeState) {
                    Text("ようこそ！", style = typography.h4, color = MaterialTheme.colors.onBackground)
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = { navController.navigate("expense_input") },
                            modifier = Modifier
                                .fillMaxWidth(0.6f) // 横幅60%（調整可能）
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                        ) {
                            Text("出費を記録する", style = typography.button, color = MaterialTheme.colors.onPrimary)
                        }
                        Button(
                            onClick = { navController.navigate("expense_list") },
                            modifier = Modifier
                                .fillMaxWidth(0.6f) // 横幅60%（調整可能）
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                        ) {
                            Text("記録一覧を見る", style = typography.button, color = MaterialTheme.colors.onSecondary)
                        }
                    }
                }
            }
        }
    }
}
