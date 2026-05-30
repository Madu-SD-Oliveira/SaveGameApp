package com.maduoliveira.savegameapp.ui.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.maduoliveira.savegameapp.ui.theme.TypographySaveGame
import com.maduoliveira.savegameapp.R
import com.maduoliveira.savegameapp.domain.model.BadgeDomain

@Composable
fun BadgesSection(
    badges: List<BadgeDomain>,
    onSeeAllClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.your_achievements),
                color = MaterialTheme.colorScheme.onBackground,
                style = TypographySaveGame.titleMedium
            )

            TextButton(
                onClick = { onSeeAllClick() },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.tertiary
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = stringResource(R.string.see_all),
                    style = TypographySaveGame.labelMedium
                )
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(badges.filter { it.isUnlocked }) { badge ->
                BadgeComponent(badge = badge)
            }
        }
    }
}

@Composable
fun BadgeComponent(badge: BadgeDomain) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.width(96.dp)
    ) {
        Image(
            painter = painterResource(id = badge.iconRes),
            contentDescription = stringResource(R.string.badge_description, stringResource(badge.nameRes)),
            modifier = Modifier
                .size(88.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Text(
            text = stringResource(badge.nameRes),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            style = TypographySaveGame.labelSmall,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}