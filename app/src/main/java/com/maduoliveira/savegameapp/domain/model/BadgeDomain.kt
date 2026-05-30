package com.maduoliveira.savegameapp.domain.model

import androidx.annotation.StringRes

data class BadgeDomain(
    val id: String,
    @StringRes val nameRes: Int,         //
    @StringRes val descriptionRes: Int,
    val iconRes: Int,
    val isUnlocked: Boolean
)

data class SettingsUiState(
    val currentXp: Int = 1250,
    val xpNeededForNextLevel: Int = 2000,
    val currentLevel: Int = 4,
    val isDarkMode: Boolean = true,
    val badges: List<BadgeDomain> = emptyList(),
    val showConfettiTrigger: Boolean = false
)