package com.maduoliveira.savegameapp.ui.screens.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maduoliveira.savegameapp.R
import com.maduoliveira.savegameapp.domain.model.BadgeDomain
import com.maduoliveira.savegameapp.domain.model.SettingsUiState
import com.maduoliveira.savegameapp.ui.screens.dashboard.customCardShape
import com.maduoliveira.savegameapp.ui.theme.TypographySaveGame
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.compose.OnParticleSystemUpdateListener
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.PartySystem
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onThemeToggle: (Boolean) -> Unit,
    onBadgeClick: (BadgeDomain) -> Unit,
    onConfettiFinished: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val party = remember {
        Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xFF00FFCC.toInt(), 0xFFFF007F.toInt(), 0xFFE9C46A.toInt()),
            position = Position.Relative(0.5, 0.3),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
        )
    }

    Box(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- HEADER ---
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text(
                        text = stringResource(R.string.settings),
                        style = TypographySaveGame.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // --- 2. STATUS HUD (XP & LEVEL) ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = customCardShape,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = stringResource(R.string.level, uiState.currentLevel), style = TypographySaveGame.labelMedium)
                            Text(
                                text = "${uiState.currentXp} / ${uiState.xpNeededForNextLevel} XP",
                                style = TypographySaveGame.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // --- Progress XP bar
                        val progressFraction = uiState.currentXp.toFloat() / uiState.xpNeededForNextLevel.toFloat()
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(14.dp)
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(progressFraction)
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(2.dp)
                                    )
                            )
                        }
                    }
                }
            }

            // --- System Mode setup ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = customCardShape,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = stringResource(R.string.dark_mode), style = TypographySaveGame.bodyLarge)
                            Text(
                                text = stringResource(R.string.alternar_apar_ncia_do_sistema),
                                style = TypographySaveGame.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            )
                        }
                        Switch(
                            checked = uiState.isDarkMode,
                            onCheckedChange = onThemeToggle,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.tertiary,
                                checkedTrackColor = MaterialTheme.colorScheme.onSecondary
                            )
                        )
                    }
                }
            }

            // --- BADGES SECTION ---
            item {
                Text(
                    text = stringResource(R.string.achievements),
                    style = TypographySaveGame.labelMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    val chunks = uiState.badges.chunked(3)
                    for (rowItems in chunks) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            for (badge in rowItems) {
                                Box(modifier = Modifier.weight(1f)) {
                                    BadgeItem(badge = badge, onClick = { onBadgeClick(badge) })
                                }
                            }
                            if (rowItems.size < 3) {
                                repeat(3 - rowItems.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }

        // --- CONFETTI Layer
        if (uiState.showConfettiTrigger) {
            KonfettiView(
                modifier = Modifier.fillMaxSize(),
                parties = listOf(party),
                updateListener = object : OnParticleSystemUpdateListener {
                    override fun onParticleSystemEnded(system: PartySystem, activeSystems: Int) {
                        if (activeSystems == 0) {
                            onConfettiFinished()
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadgeItem(
    badge: BadgeDomain,
    onClick: () -> Unit
) {
    // Filter Monochrome
    val grayscaleMatrix = remember { ColorMatrix().apply { setToSaturation(0f) } }
    val badgeName = stringResource(id = badge.nameRes)
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (badge.isUnlocked) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.05f)
        ),
        border = if (badge.isUnlocked) CardDefaults.outlinedCardBorder() else null
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = badge.iconRes),
                contentDescription = badgeName,
                modifier = Modifier.size(56.dp),
                colorFilter = if (badge.isUnlocked) null else ColorFilter.colorMatrix(grayscaleMatrix)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = badgeName,
                style = TypographySaveGame.bodyMedium,
                textAlign = TextAlign.Center,
                color = if (badge.isUnlocked) MaterialTheme.colorScheme.onBackground
                else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                maxLines = 1
            )
        }
    }
}