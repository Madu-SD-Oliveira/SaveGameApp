package com.maduoliveira.savegameapp.ui.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maduoliveira.savegameapp.R
import com.maduoliveira.savegameapp.ui.theme.SaveGameAppTheme
import com.maduoliveira.savegameapp.ui.theme.TypographySaveGame

@Composable
fun CardBalance(
    balance: Double,
    isBalanceVisible: Boolean,
    percentageChange: Double,
    onToggleVisibility: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPositiveChange = percentageChange >= 0
    val indicatorIcon = if (isPositiveChange) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
    val indicatorColor = if (isPositiveChange) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.secondary
    Card(
        modifier = modifier,
        shape = customCardShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
        ) {
            // Background image que preenche o card
            Image(
                painter = painterResource(id = R.drawable.img_01),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.8f), MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f))
                        )
                    )
            )
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.current_balance),
                        style = TypographySaveGame.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    IconButton(onClick = onToggleVisibility) {
                        Icon(
                            painter = painterResource(
                                id = if (isBalanceVisible) R.drawable.eye else R.drawable.remove_eye
                            ),
                            contentDescription = "Toggle Visibility",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
                Text(
                    text = if (isBalanceVisible) "${String.format("%.2f", balance)} €" else "••••••",
                    style = TypographySaveGame.titleLarge,
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.surface
                )
                val sign = if (isPositiveChange) "+" else "-"
                Text(
                    text = stringResource(
                        id = R.string.monthly_status_percentage,
                        sign,
                        kotlin.math.abs(percentageChange)
                    ),
                    style = TypographySaveGame.titleMedium,
                    color = indicatorColor
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Saldo Visível")
@Composable
fun CardBalanceVisiblePreview() {
    SaveGameAppTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            CardBalance(
                balance = 7350.42,
                isBalanceVisible = true,
                percentageChange = 40.0,
                onToggleVisibility = {}
            )
        }
    }
}
@Preview(showBackground = true, name = "Saldo Oculto")
@Composable
fun CardBalanceHiddenPreview() {
    SaveGameAppTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            CardBalance(
                balance = 7350.42,
                isBalanceVisible = false,
                percentageChange = 5.4,
                onToggleVisibility = {}
            )
        }
    }
}
