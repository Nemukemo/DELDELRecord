package com.example.deldelrecord.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.deldelrecord.ui.ExpenseInputScreen
import com.example.deldelrecord.ui.ExpenseListScreen
import com.example.deldelrecord.ui.HomeScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("expense_input") { ExpenseInputScreen(navController) }
        composable("expense_list") { ExpenseListScreen(navController) }
    }
}
