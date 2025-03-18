package com.example.deldelrecord.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.filled.PhotoCamera  // ← これを追加！
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.deldelrecord.ui.screens.ExpenseInputScreen
import com.example.deldelrecord.ui.screens.RecognitionScreen
import com.example.deldelrecord.ui.screens.ExpenseListScreen

// BottomNav用の画面定義
sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Input : BottomNavItem("input", "手入力", Icons.Filled.Edit)
    object Recognition : BottomNavItem("recognition", "画像認識", Icons.Filled.PhotoCamera)  // ← 修正！
    object List : BottomNavItem("list", "一覧", Icons.Filled.List)
}

@Composable
fun BottomNavScreen() {
    val navController = rememberNavController()

    // ナビゲーション用の画面リスト
    val bottomNavItems = listOf(
        BottomNavItem.Input,
        BottomNavItem.Recognition,
        BottomNavItem.List
    )

    Scaffold(
        bottomBar = {
            BottomNavigation {
                val currentDestination = navController.currentBackStackEntryAsState().value?.destination
                bottomNavItems.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.route == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                // 同じ画面を再選択してもBackStackを再生成しないようにする
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Input.route,
            modifier = androidx.compose.ui.Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Input.route) { ExpenseInputScreen(navController) }
            composable(BottomNavItem.Recognition.route) { RecognitionScreen() }
            composable(BottomNavItem.List.route) { ExpenseListScreen(navController) }
        }
    }
}
