package com.maduoliveira.savegameapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import com.maduoliveira.savegameapp.R

// 1. Definição das Famílias de Fontes
val RetroFontFamily = FontFamily(
    Font(R.font.press_start_2p, FontWeight.Normal)
)

val ModernFontFamily = FontFamily(
    Font(R.font.chakra_petch_light, FontWeight.Thin),
    Font(R.font.chakra_petch_regular, FontWeight.Normal),
    Font(R.font.chakra_petch_medium, FontWeight.Medium)
)

// 2. Configuração da Tipografia do Material 3
val TypographySaveGame = Typography(

    // USADO PARA: Headers Principais de Tela (ex: "CRONOLOGIA FINANCEIRA")
    displayLarge = TextStyle(
        fontFamily = RetroFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 1.sp
    ),

    displaySmall = TextStyle(
        fontFamily = RetroFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 26.sp,
        letterSpacing = 1.sp
    ),

    // USADO PARA: Títulos de Cards ou Seções importantes (ex: "SALDO TOTAL", "CONQUISTAS")

    headlineMedium = TextStyle(
        fontFamily = ModernFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = ModernFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp
    ),

    // USADO PARA: Subtítulos, Categorias e Valores secundários (ex: "Nível 4", "Mercado")
    titleMedium = TextStyle(
        fontFamily = ModernFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    ),

    // USADO PARA: Textos corridos, descrições e inputs (ex: "Supermercado do bairro", "01/01/2026")
    bodyLarge = TextStyle(
        fontFamily = ModernFontFamily,
        fontWeight = FontWeight.Thin,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),

    // USADO PARA: Pequenas marcações ou detalhes de rodapé (ex: "LOADING...", "UP", "DOWN")
    labelSmall = TextStyle(
        fontFamily = ModernFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.5.sp
    )
)