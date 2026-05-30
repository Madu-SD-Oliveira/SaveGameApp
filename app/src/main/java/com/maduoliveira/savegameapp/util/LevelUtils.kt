package com.maduoliveira.savegameapp.util

import com.maduoliveira.savegameapp.R
import com.maduoliveira.savegameapp.domain.model.BadgeDomain

object LevelUtils {
    const val XP_PER_LEVEL = 5000

    fun calculateTotalXp(totalIncomes: Double): Int =
        (totalIncomes * 0.1).toInt().coerceAtLeast(0)

    fun xpToLevel(totalXp: Int): Int {
        val lvl = totalXp / XP_PER_LEVEL + 1
        return lvl.coerceIn(1, 10)
    }

    fun getLevelName(level: Int): String {
        return when (level.coerceIn(1, 10)) {
            1 -> "EXPLORER"
            2 -> "SAVER"
            3 -> "BUILDER"
            4 -> "STRATEGIST"
            5 -> "ADVOCATE"
            6 -> "CHAMPION"
            7 -> "MENTOR"
            8 -> "GUARDIAN"
            9 -> "LEGEND"
            10 -> "MYTHIC"
            else -> "UNKNOWN"
        }
    }

    fun xpForNextLevel(totalXp: Int): Int {
        val currentLevel = xpToLevel(totalXp)
        if (currentLevel >= 10) return 0
        val nextLevelXp = currentLevel * XP_PER_LEVEL
        return (nextLevelXp - totalXp).coerceAtLeast(0)
    }

    fun generateBadges(totalXpAbsoluto: Int): List<BadgeDomain> {
        return listOf(
            BadgeDomain("1", R.string.badge_wood_title, R.string.badge_wood_desc, R.drawable.wood2, totalXpAbsoluto >= 0),
            BadgeDomain("2", R.string.badge_iron_title, R.string.badge_iron_desc, R.drawable.iron2, totalXpAbsoluto >= 5000),
            BadgeDomain("3", R.string.badge_quartz_title, R.string.badge_quartz_desc, R.drawable.quartzo2, totalXpAbsoluto >= 10000),
            BadgeDomain("4", R.string.badge_bronze_title, R.string.badge_bronze_desc, R.drawable.bronze1, totalXpAbsoluto >= 15000),
            BadgeDomain("5", R.string.badge_silver_title, R.string.badge_silver_desc, R.drawable.silver2, totalXpAbsoluto >= 20000),
            BadgeDomain("6", R.string.badge_gold_title, R.string.badge_gold_desc, R.drawable.gold2, totalXpAbsoluto >= 25000),
            BadgeDomain("7", R.string.badge_diamond_title, R.string.badge_diamond_desc, R.drawable.diamond3, totalXpAbsoluto >= 30000)
        )
    }
}