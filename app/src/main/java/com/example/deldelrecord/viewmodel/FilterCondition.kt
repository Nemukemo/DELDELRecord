package com.example.deldelrecord.viewmodel

import java.time.LocalDate

// ViewModel内部のフィルターロジック専用の構造体
// UIや保存対象ではない点に注意
data class FilterCondition(
    val minAmount: Int? = null,
    val maxAmount: Int? = null,
    val types: List<String>? = null,
    val dateFrom: LocalDate? = null,
    val dateTo: LocalDate? = null
)
