package com.maduoliveira.savegameapp.domain.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maduoliveira.savegameapp.R
import com.maduoliveira.savegameapp.data.repository.FinancesRepository
import com.maduoliveira.savegameapp.domain.model.BadgeDomain
import com.maduoliveira.savegameapp.util.LevelUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId


class DashboardViewModel(
    private val repository: FinancesRepository
) : ViewModel() {
    private val _isBalanceVisible = MutableStateFlow(true)

    val uiState: StateFlow<DashboardUiState> = combine(
            repository.getAllTransactions,
            _isBalanceVisible
        ) { transactions, isVisible ->

            val currentYearMonth = transactions.maxByOrNull { it.date }?.date?.let { timestamp ->
                YearMonth.from(Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate())
            } ?: YearMonth.now()
            val previousYearMonth = currentYearMonth.minusMonths(1)


            val incomesCurrent = transactions
                .filter {
                    it.type == "INCOMES" &&
                            YearMonth.from(Instant.ofEpochMilli(it.date).atZone(ZoneId.systemDefault()).toLocalDate()) == currentYearMonth
                }
                .sumOf { it.value }

            val incomesPrevious = transactions
                .filter {
                    it.type == "INCOMES" &&
                            YearMonth.from(Instant.ofEpochMilli(it.date).atZone(ZoneId.systemDefault()).toLocalDate()) == previousYearMonth
                }
                .sumOf { it.value }

            val expensesCurrent = transactions
                .filter {
                    it.type == "EXPENSES" &&
                            YearMonth.from(Instant.ofEpochMilli(it.date).atZone(ZoneId.systemDefault()).toLocalDate()) == currentYearMonth
                }
                .sumOf { it.value }

            val expensesPrevious = transactions
                .filter {
                    it.type == "EXPENSES" &&
                            YearMonth.from(Instant.ofEpochMilli(it.date).atZone(ZoneId.systemDefault()).toLocalDate()) == previousYearMonth
                }
                .sumOf { it.value }

            val balanceCurrent = incomesCurrent - expensesCurrent

            // calcula variação percentual (defensivo)
            fun percentChange(current: Double, previous: Double): Double {
                return when {
                    previous == 0.0 && current == 0.0 -> 0.0
                    previous == 0.0 -> 100.0
                    else -> ((current - previous) / previous) * 100.0
                }
            }

            // Para o Saldo: Porcentagem do saldo no mês atual (em relação ao total de receitas do mês)
            val balanceMonthPercent = if (incomesCurrent > 0) (balanceCurrent / incomesCurrent) * 100.0 else if (expensesCurrent > 0) -100.0 else 0.0

            val totalInc = transactions.filter { it.type == "INCOMES" }.sumOf { it.value }
            val totalExp = transactions.filter { it.type == "EXPENSES" }.sumOf { it.value }
            val balance = totalInc - totalExp

            val totalXpAbsoluto = LevelUtils.calculateTotalXp(totalInc)
            val currentLvl = LevelUtils.xpToLevel(totalXpAbsoluto)
            val lvlName = LevelUtils.getLevelName(currentLvl)
            val xpNoNivelAtual = totalXpAbsoluto % LevelUtils.XP_PER_LEVEL

            val percentIncomesChange = percentChange(incomesCurrent, incomesPrevious)
            val percentExpensesChange = percentChange(expensesCurrent, expensesPrevious)

            val baseBadges = listOf(
                BadgeDomain("1", R.string.badge_wood_title, R.string.badge_wood_desc, R.drawable.wood2, totalXpAbsoluto >= 0),
                BadgeDomain("2", R.string.badge_iron_title, R.string.badge_iron_desc, R.drawable.iron2, totalXpAbsoluto >= 1000),
                BadgeDomain("3", R.string.badge_quartz_title, R.string.badge_quartz_desc, R.drawable.quartzo2, totalXpAbsoluto >= 2000),
                BadgeDomain("4", R.string.badge_bronze_title, R.string.badge_bronze_desc, R.drawable.bronze1, totalXpAbsoluto >= 3000),
                BadgeDomain("5", R.string.badge_silver_title, R.string.badge_silver_desc, R.drawable.silver2, totalXpAbsoluto >= 4000),
                BadgeDomain("6", R.string.badge_gold_title, R.string.badge_gold_desc, R.drawable.gold2, totalXpAbsoluto >= 5000),
                BadgeDomain("7", R.string.badge_diamond_title, R.string.badge_diamond_desc, R.drawable.diamond3, totalXpAbsoluto >= 7000)
            )

            DashboardUiState(
                totalBalance = balance,
                totalIncomes = totalInc,
                totalExpenses = totalExp,
                percentageChange = balanceMonthPercent,
                percentIncomesChange = percentIncomesChange,
                percentageExpensesChange = percentExpensesChange,
                recentTransactions = transactions.take(5),
                currentLevel = currentLvl,
                levelName = lvlName,
                currentXp = if (currentLvl >= 10) LevelUtils.XP_PER_LEVEL else xpNoNivelAtual,
                xpNeededForNextLevel = LevelUtils.XP_PER_LEVEL,
                badges = LevelUtils.generateBadges(totalXpAbsoluto),
                isBalanceVisible = isVisible,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardUiState()
        )

    fun toggleBalanceVisibility() {
        _isBalanceVisible.update { !it }
    }
}