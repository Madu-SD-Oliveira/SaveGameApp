package com.maduoliveira.savegameapp.ui.screens.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun TransactionOutlinedRow(
    transaction: Transaction,
    modifier: Modifier = Modifier
) {
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val dateText = dateFormatter.format(Date(transaction.date))

    val isIncome = transaction.type == stringResource(R.string.incomes)
    val badgeColor = if (isIncome) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    val prefix = if (isIncome) stringResource(R.string.plus_rs) else stringResource(R.string.minus_rs)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = CardDefaults.outlinedShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Categoria Principal
                    Text(
                        text = transaction.name,
                        style = TypographySaveGame.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(badgeColor)
                    )
                }

                // Description
                Text(
                    text = transaction.description.ifBlank { stringResource(R.string.no_description) },
                    style = TypographySaveGame.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.padding(vertical = 2.dp)
                )

                // Formated date
                Text(
                    text = stringResource(R.string.done_in, dateText),
                    style = TypographySaveGame.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                )
            }

            Text(
                text = String.format(LocalLocale.current.platformLocale, "%s%.2f", prefix, transaction.value),
                style = TypographySaveGame.labelMedium,
                color = badgeColor
            )
        }
    }
}