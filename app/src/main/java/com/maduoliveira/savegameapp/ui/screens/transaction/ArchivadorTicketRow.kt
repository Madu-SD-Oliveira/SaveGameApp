package com.maduoliveira.savegameapp.ui.screens.transaction

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maduoliveira.savegameapp.R
import com.maduoliveira.savegameapp.domain.transaction.TransactionUIModel
import com.maduoliveira.savegameapp.ui.theme.*
import com.maduoliveira.savegameapp.ui.theme.TypographySaveGame

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModernTransactionCard(
    uiModel: TransactionUIModel,
    isExpanded: Boolean,
    onEditClick: () -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    index: Int = 0
) {
    val transaction = uiModel.transaction
    val isIncome = transaction.type == "INCOMES"

    val badgeColor = if (isIncome) colorScheme.primary else colorScheme.error
    val prefix = if (isIncome) stringResource(R.string.plus_rs) else stringResource(R.string.minus_rs)

    val isDark = isSystemInDarkTheme()

    val palette = SaveGameTheme.modernPalette
    val backgroundColor = remember(transaction.id, palette) {
        if (palette.isNotEmpty()) {
            palette[index % palette.size]
        } else {
            if (isDark) Color(0xFF161B22) else Color(0xFFE5E5E5)
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            // Header Row: Icon | Name | Dot | Value
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Category/Channel Icon (Small Square)
                val iconRes = if (isIncome) uiModel.channelIcon else uiModel.categoryIcon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = if (isDark) 0.1f else 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (iconRes != null && iconRes != 0) {
                        Image(
                            painter = painterResource(id = iconRes),
                            contentDescription = if (isIncome) uiModel.channelName else uiModel.categoryName,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = if (isIncome) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = badgeColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = transaction.name,
                        style = TypographySaveGame.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = if (isDark) Color.White else Color(0xFF2D2D2D)
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = if (isIncome) uiModel.channelName else uiModel.categoryName,
                        style = TypographySaveGame.labelSmall.copy(
                            color = (if (isDark) Color.White else Color(0xFF2D2D2D)).copy(alpha = 0.6f)
                        )
                    )
                }

                // Indicator
                /*Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(RoundedCornerShape(50))
                        .background(badgeColor)
                )*/

                Spacer(modifier = Modifier.width(12.dp))

                // Value
                Text(
                    text = String.format(LocalLocale.current.platformLocale, "%s%.2f", prefix, transaction.value),
                    style = TypographySaveGame.bodyLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = if (isIncome) {
                            if (isDark) Color(0xFF81C784) else Color(0xFF1B5E20)
                        } else {
                            if (isDark) Color(0xFFE57373) else Color(0xFFB71C1C)
                        }
                    )
                )
            }

            // Expanded Content
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    if (transaction.description.isNotBlank()) {
                        Text(
                            text = transaction.description,
                            style = TypographySaveGame.bodyMedium.copy(
                                color = (if (isDark) Color.White else Color(0xFF2D2D2D)).copy(alpha = 0.8f),
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "${stringResource(R.string.sector)} ${uiModel.sectorName}",
                            style = TypographySaveGame.labelSmall.copy(
                                color = (if (isDark) Color.White else Color(0xFF2D2D2D)).copy(alpha = 0.5f)
                            )
                        )

                        Button(
                            onClick = onEditClick,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDark) Color.White else Color(0xFF2D2D2D),
                                contentColor = if (isDark) Color.Black else Color.White
                            ),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.editar).uppercase(),
                                style = TypographySaveGame.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}