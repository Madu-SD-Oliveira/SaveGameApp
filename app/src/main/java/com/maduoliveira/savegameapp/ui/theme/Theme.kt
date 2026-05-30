package com.maduoliveira.savegameapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


// --- PALETA RETRO-CYBERPUNK (DARK MODE) ---
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF658963),
    secondary = Color(0xFFAC5045),
    tertiary = Color(0xFFE0A15E),
    onTertiary = Color(0xFFA0C398),
    background = Color(0xFF0D1117),
    surface = Color(0xFF161B22),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFFFFFFFF),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFE2E8F0)
)

// --- PALETA RETRO-CONSOLE NES (LIGHT MODE) ---
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF004224),
    secondary = Color(0xFFD32F2F),
    tertiary = Color(0xFFE0A15E),
    onTertiary = Color(0xFF9FC197),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFE5E5E5),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1A1D20),
    onSurface = Color(0xFF2A2A2A)
)

val LocalModernPalette = staticCompositionLocalOf<List<Color>> {
    emptyList()
}

object SaveGameTheme {
    val modernPalette: List<Color>
        @Composable
        get() = LocalModernPalette.current
}

@Composable
fun SaveGameAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val modernColors = if (darkTheme) {
        listOf(CardBeigeDarkGray, CardSageDarkGray, CardPeachDarkGray, CardGreyDarkGray, CardRoseDarkGray, CardBlueDarkGray)
    } else {
        listOf(CardBeigeLightGray, CardSageLightGray, CardPeachLightGray, CardGreyLightGray, CardRoseLightGray, CardBlueLightGray)
    }

    CompositionLocalProvider(LocalModernPalette provides modernColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = TypographySaveGame,
            content = content
        )
    }
}