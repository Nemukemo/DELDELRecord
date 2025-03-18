package com.example.deldelrecord.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RecognitionScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("画像認識入力") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("アップデートで実装します", style = MaterialTheme.typography.h5)
        }
    }
}
