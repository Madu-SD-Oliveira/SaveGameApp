package com.maduoliveira.savegameapp.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maduoliveira.savegameapp.R
import com.maduoliveira.savegameapp.ui.theme.TypographySaveGame

@Composable
fun CardInfo(
    title: String,
    value: Double,
    isIncome: Boolean,
    isBalanceVisible: Boolean,
    percentageChange: Double,
    modifier: Modifier = Modifier
) {
    val contentColor = if (isIncome) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

    val isPositiveChange = percentageChange >= 0
    val indicatorIcon = if (isPositiveChange) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
    val indicatorColor = if (isPositiveChange) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

    val iconPainter = if (isIncome) {
        painterResource(id = R.drawable.trending_up)
    } else {
        painterResource(id = R.drawable.trending_down)
    }
    Card(
        modifier = modifier,
        shape = CardDefaults.shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp))
             {
                Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
                 ) {
                    IconWithBackground(
                        painter = iconPainter,
                        contentDescription = null,
                        size = 28.dp,
                        tint = contentColor.copy(alpha = 0.15F),
                        iconTint = contentColor,
                    )
                    Text(
                        text = title,
                        style = TypographySaveGame.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Column( verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (isBalanceVisible) "$${String.format("%.2f", value)}" else "••••",
                        style = TypographySaveGame.bodyLarge,
                        fontSize = 32.sp,
                        color = contentColor
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            imageVector = indicatorIcon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = indicatorColor
                        )
                        val sign = if (isPositiveChange) "+" else "-"

                        Text(
                            text = stringResource(
                                id = R.string.monthly_percentage_change,
                                sign,
                                kotlin.math.abs(percentageChange)
                            ),
                            style = TypographySaveGame.labelSmall,
                            color = indicatorColor
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun IconWithBackground(
    painter: Painter,
    contentDescription: String?,
    size: Dp = 40.dp,
    tint: Color,
    iconTint: Color,
    cornerRadius: Dp = 8.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(tint),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = Modifier.size(size * 0.55f)
        )
    }
}
