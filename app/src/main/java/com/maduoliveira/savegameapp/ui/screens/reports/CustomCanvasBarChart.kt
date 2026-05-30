package com.maduoliveira.savegameapp.ui.screens.reports

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maduoliveira.savegameapp.domain.model.ReportsCategoryDomain
import com.maduoliveira.savegameapp.ui.theme.TypographySaveGame

@Composable
fun CustomCanvasBarChart(
    data: List<ReportsCategoryDomain>,
    chartColors: List<Color>,
    animationProgress: Float
) {
    val locale = LocalLocale.current.platformLocale
    val labelStyle = TypographySaveGame.labelMedium.copy(
        color = MaterialTheme.colorScheme.onBackground
    )
    val gridColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
    val maxVal = (data.maxOfOrNull { it.total }?.toFloat() ?: 1f).coerceAtLeast(1f)

    // Configuração de dimensões
    val barWidth = 45.dp
    val barSpacing = 20.dp
    val chartHeight = 180.dp
    val yAxisWidth = 45.dp

    fun formatValue(value: Float): String {
        return when {
            value >= 1000f -> String.format(locale, "%.1fk", value / 1000f)
            else -> value.toInt().toString()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(chartHeight + 40.dp)
            .padding(top = 8.dp)
    ) {
        // --- EIXO Y (Fixo) ---
        Canvas(
            modifier = Modifier
                .width(yAxisWidth)
                .height(chartHeight)
        ) {
            val steps = 4
            for (i in 0..steps) {
                val fraction = i.toFloat() / steps
                val y = size.height - (fraction * size.height)
                val value = maxVal * fraction

                drawContext.canvas.nativeCanvas.drawText(
                    formatValue(value),
                    0f,
                    y + 5.dp.toPx(),
                    Paint().apply {
                        this.color = labelStyle.color.toArgb()
                        this.textSize = 10.sp.toPx()
                        this.textAlign = Paint.Align.LEFT
                    }
                )
            }
        }

        // --- ÁREA SCROLLAVEL (BARRAS + EIXO X) ---
        Box(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(rememberScrollState())
        ) {
            val totalWidth = (barWidth + barSpacing) * data.size + 20.dp

            Canvas(
                modifier = Modifier
                    .width(totalWidth)
                    .height(chartHeight + 40.dp)
            ) {
                val canvasHeight = chartHeight.toPx()
                val barWidthPx = barWidth.toPx()
                val barSpacingPx = barSpacing.toPx()

                // Linhas de Grade Horizontais
                val steps = 4
                for (i in 0..steps) {
                    val y = canvasHeight - (i.toFloat() / steps * canvasHeight)
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1f
                    )
                }

                data.forEachIndexed { index, item ->
                    val x = index * (barWidthPx + barSpacingPx)
                    val barHeight = (item.total.toFloat() / maxVal) * canvasHeight * animationProgress
                    val color = chartColors[index % chartColors.size]

                    // Desenha a Barra
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(x, canvasHeight - barHeight),
                        size = Size(barWidthPx, barHeight),
                        cornerRadius = CornerRadius(12f, 12f)
                    )

                    // Label X (Nome da Categoria)
                    val shortName = if (item.categoryName.length > 4) {
                        item.categoryName.take(3) + ".."
                    } else item.categoryName

                    drawContext.canvas.nativeCanvas.drawText(
                        shortName.uppercase(),
                        x + (barWidthPx / 2),
                        canvasHeight + 25.dp.toPx(),
                        Paint().apply {
                            this.color = labelStyle.color.toArgb()
                            this.textSize = 9.sp.toPx()
                            this.textAlign = Paint.Align.CENTER
                            this.isFakeBoldText = true
                        }
                    )
                }
            }
        }
    }
}
