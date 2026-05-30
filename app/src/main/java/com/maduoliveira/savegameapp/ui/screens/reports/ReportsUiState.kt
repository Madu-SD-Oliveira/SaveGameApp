package com.maduoliveira.savegameapp.ui.screens.reports

import com.maduoliveira.savegameapp.domain.model.ReportsCategoryDomain

data class ReportsUiState(
    val selectedPeriod: String = "1 Month", // "1 Month" ou "3 Month"
    val selectedType: String = "EXPENSES",   // "INCOMES" ou "EXPENSES"
    val category: List<ReportsCategoryDomain> = emptyList(),
    val totalPeriodo: Double = 0.0,
    val total: Double = 0.0,
    val jsonExport: String? = null,
    val isLoading: Boolean = false
)