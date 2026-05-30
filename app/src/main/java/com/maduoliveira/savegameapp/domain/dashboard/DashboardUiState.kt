package com.maduoliveira.savegameapp.domain.dashboard

import com.maduoliveira.savegameapp.domain.model.BadgeDomain
import com.maduoliveira.savegameapp.domain.model.Transaction

data class DashboardUiState(
    // finance data
    val totalBalance: Double = 0.0,
    val totalIncomes: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val percentageChange: Double = 0.0,
    val recentTransactions: List<Transaction> = emptyList(),
    val percentIncomesChange: Double = 0.0,
    val percentageExpensesChange: Double = 0.0,

    // gamification
    val currentLevel: Int = 1,
    val levelName: String = "",
    val currentXp: Int = 0,
    val xpNeededForNextLevel: Int = 5000,
    val recentAchievement: String? = null,
    val badges: List<BadgeDomain> = emptyList(),

    // Estado de Interface
    val isBalanceVisible: Boolean = true,
    val isLoading: Boolean = true
)