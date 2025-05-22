package com.example.deldelrecord.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Int,         // 金額
    val type: String,        // 出費の種類（修学費、食費、娯楽費）
    val date: String,        // 年月日（YYYY-MM-DD）
    val memo: String? = null // メモ（未入力可）
)
