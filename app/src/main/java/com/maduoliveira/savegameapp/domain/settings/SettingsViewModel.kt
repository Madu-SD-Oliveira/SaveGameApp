package com.maduoliveira.savegameapp.domain.settings

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maduoliveira.savegameapp.R
import com.maduoliveira.savegameapp.data.repository.FinancesRepository
import com.maduoliveira.savegameapp.domain.model.BadgeDomain
import com.maduoliveira.savegameapp.domain.model.SettingsUiState
import com.maduoliveira.savegameapp.util.LevelUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SettingsViewModel(
    private val repository: FinancesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        observePlayerProgress()
    }

    private fun observePlayerProgress() {
        viewModelScope.launch {
            repository.getAllTransactions.collect { transactions ->
                val totalIncomes = transactions.filter { it.type == "INCOMES" }.sumOf { it.value }

                val totalXpAbsoluto = LevelUtils.calculateTotalXp(totalIncomes)
                val currentLvl = LevelUtils.xpToLevel(totalXpAbsoluto)
                val xpNoNivelAtual = totalXpAbsoluto % LevelUtils.XP_PER_LEVEL

                _uiState.update { state ->
                    state.copy(
                        currentLevel = currentLvl,
                        currentXp = if (currentLvl >= 10) LevelUtils.XP_PER_LEVEL else xpNoNivelAtual,
                        xpNeededForNextLevel = LevelUtils.XP_PER_LEVEL,
                        badges = LevelUtils.generateBadges(totalXpAbsoluto)
                    )
                }
            }
        }
    }

    fun toggleTheme(isDark: Boolean) {
        _uiState.update { it.copy(isDarkMode = isDark) }
    }

    fun triggerConfetti() {
        _uiState.update { it.copy(showConfettiTrigger = true) }
    }

    fun resetConfetti() {
        _uiState.update { it.copy(showConfettiTrigger = false) }
    }
}
