package com.maduoliveira.savegameapp.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maduoliveira.savegameapp.domain.model.Transaction
import com.maduoliveira.savegameapp.ui.theme.TypographySaveGame
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import com.maduoliveira.savegameapp.R

@Composable
fun CardRecents(
    transactions: List<Transaction>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = inverseCustomCardShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.recent_mission),
                style = TypographySaveGame.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
            )

            if (transactions.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_register),
                    style = TypographySaveGame.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                val mostRecent = transactions
                    .sortedByDescending { it.date }
                    .take(5)

                mostRecent.forEach { transaction ->
                    CardRecentItems(transaction = transaction)
                }
            }
        }
    }
}
@Composable
fun CardRecentItems(
    transaction: Transaction,
    modifier: Modifier = Modifier
) {
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val dateText = dateFormatter.format(Date(transaction.date))

    val isIncome = transaction.type == "INCOMES"
    val valueColor =
        if (isIncome) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val prefix = if (isIncome) stringResource(R.string.plus_rs) else stringResource(R.string.minus_rs)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.name,
                style = TypographySaveGame.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = transaction.description.ifBlank { stringResource(R.string.no_description) },
                style = TypographySaveGame.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            /*Text(
                text = transaction.categoryId?.toString() ?: "",
                style = TypographySaveGame.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )*/
            Text(
                text = dateText,
                style = TypographySaveGame.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }

        Text(
            text = String.format(LocalLocale.current.platformLocale, "%s%.2f", prefix, transaction.value),
            style = TypographySaveGame.labelMedium,
            color = valueColor
        )
    }
}
val inverseCustomCardShape = RoundedCornerShape(
    topStart = 20.dp,
    topEnd = 0.dp,
    bottomEnd = 20.dp,
    bottomStart = 0.dp
)