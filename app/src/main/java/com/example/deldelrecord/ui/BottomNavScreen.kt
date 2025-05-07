package com.example.deldelrecord.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.deldelrecord.ui.Screen.SettingsScreen
import com.example.deldelrecord.ui.screens.ExpenseInputScreen
import com.example.deldelrecord.ui.Screen.ExpenseListScreen

// BottomNav用の画面定義
sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Input : BottomNavItem("input", "手入力", Icons.Filled.Edit)
    object List : BottomNavItem("list", "一覧", Icons.Filled.List)
    object Settings : BottomNavItem("settings", "設定", Icons.Filled.Settings)
}

@Composable
fun BottomNavScreen() {
    val navController = rememberNavController()

    // ナビゲーション用の画面リスト
    val bottomNavItems = listOf(
        BottomNavItem.Input,
        BottomNavItem.List,
        BottomNavItem.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                // リストを直接ループする
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
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
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Input.route) { ExpenseInputScreen(navController) }
            composable(BottomNavItem.List.route) { ExpenseListScreen(navController) }
            composable(BottomNavItem.Settings.route) {
                SettingsScreen(
                    isDarkTheme = false,
                    onThemeToggle = { /* TODO */ },
                    onExpenseTypeSettingsClick = { /* TODO */ }
                )
            }

        }
    }
}
