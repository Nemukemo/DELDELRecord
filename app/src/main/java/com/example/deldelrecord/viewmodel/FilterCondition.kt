package com.example.deldelrecord.viewmodel

import java.time.LocalDate

// ViewModel内部のフィルターロジック専用の構造体
// UIや保存対象ではない点に注意
data class FilterCondition(
    var minAmount: Int? = null,
    var maxAmount: Int? = null,
    var types: List<String>? = emptyList(),
    var dateFrom: LocalDate? = null,
    var dateTo: LocalDate? = null
)
