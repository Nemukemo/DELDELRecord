package com.example.deldelrecord.ui.Screen

import android.text.Layout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography


@Composable
fun SettingsScreen(
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    onExpenseTypeSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "設定",
            style = MaterialTheme.typography.headlineSmall,
        )

        // テーマ切り替えスイッチ
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "アップデートで実装します☆",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        // 出費タイプ設定ボタン
        Button(
            onClick = onExpenseTypeSettingsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("出費タイプの編集")
        }
    }
}
//TODO：トグルスイッチの実装
//TODO；ライト/ダークのテーマ切り替えの実装
//TODO：出費タイプの設定画面の作成(重要度高め)
