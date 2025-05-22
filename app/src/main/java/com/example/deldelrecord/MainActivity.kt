package com.example.deldelrecord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.internal.enableLiveLiterals
import com.example.deldelrecord.ui.BottomNavScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            BottomNavScreen()
        }
    }
}
//TODO: アプリ全体にライトテーマとダークテーマを設ける