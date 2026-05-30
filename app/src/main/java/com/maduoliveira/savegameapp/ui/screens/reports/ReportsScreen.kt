package com.maduoliveira.savegameapp.ui.screens.reports

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maduoliveira.savegameapp.R
import com.maduoliveira.savegameapp.domain.reports.ReportsUiState
import com.maduoliveira.savegameapp.ui.screens.dashboard.customCardShape
import com.maduoliveira.savegameapp.ui.theme.TypographySaveGame

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    uiState: ReportsUiState,
    onPeriodSelect: (String) -> Unit,
    onTypeSelect: (String) -> Unit,
    onExportClick: () -> Unit,
    onExportHandled: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val chartColors = remember {
        listOf(
            Color(0xFF2E4031),
            Color(0xFF556B2F),
            Color(0xFF8B352B),
            Color(0xFFA3BC8F),
            Color(0xFFD4A373),
            Color(0xFFE9C46A)
        )
    }
    val typeOptions = listOf(
        "INCOMES" to stringResource(R.string.incomes),
        "EXPENSES" to stringResource(R.string.expenses)
    )
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(uiState.category) {
        if (uiState.category.isNotEmpty()) {
            animationProgress.snapTo(0f)
            animationProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000)
            )
        }
    }

    LaunchedEffect(uiState.jsonExport) {
        uiState.jsonExport?.let { jsonText ->
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, jsonText)
                type = "application/json"
            }
            val shareIntent = Intent.createChooser(
                sendIntent,
                context.getString(R.string.savegame_backup)
            )
            context.startActivity(shareIntent)
            onExportHandled()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // --- HEADER ---
        item {
            Text(
                text = stringResource(R.string.statistics),
                style = TypographySaveGame.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign =  TextAlign.Center,
            )
        }

        // --- FILTERS ---
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    stringResource(R.string.one_month),
                    stringResource(R.string.three_months)
                ).zip(listOf("1 Month", "3 Month")).forEach { (periodLabel, periodValue) ->
                    val isSelected = uiState.selectedPeriod == periodValue
                    InputChip(
                        selected = isSelected,
                        onClick = { onPeriodSelect(periodValue) },
                        label = { Text(periodLabel) },
                        shape = customCardShape,
                        colors = InputChipDefaults.inputChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                typeOptions.forEach { (typeValue, typeLabel) ->
                    val isSelected = uiState.selectedType == typeValue

                    val selectedContainerColor = if (typeValue == "INCOMES") {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.secondary
                    }

                    InputChip(
                        selected = isSelected,
                        onClick = { onTypeSelect(typeValue) },
                        label = {
                            Text(
                                text = typeLabel,
                                style = TypographySaveGame.labelSmall,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        },
                        modifier = Modifier.weight(1f),
                        border = null,
                        colors = InputChipDefaults.inputChipColors(
                            selectedContainerColor = selectedContainerColor,
                            selectedLabelColor = MaterialTheme.colorScheme.onSecondary,
                            disabledContainerColor = MaterialTheme.colorScheme.onSurface,
                            containerColor = MaterialTheme.colorScheme.onSurface,
                            labelColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }
        }

        // --- Animated Graphs ---
        item {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.category.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp), contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.no_data), style = TypographySaveGame.bodyMedium)
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(
                            alpha = 0.3f
                        )
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.total_rs, uiState.totalPeriodo),
                            style = TypographySaveGame.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        ) {
                            CustomCanvasBarChart(
                                data = uiState.category,
                                chartColors = chartColors,
                                animationProgress = animationProgress.value
                            )
                        }
                    }
                }
            }
        }

        // --- Label List ---
        if (!uiState.isLoading && uiState.category.isNotEmpty()) {
            items(uiState.category.zip(chartColors)) { (category, labelColor) ->
                val percentual =
                    if (uiState.totalPeriodo > 0.0) (category.total / uiState.totalPeriodo).toFloat() else 0f
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = customCardShape,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(labelColor)
                                )

                                Column {
                                    Text(
                                        text = category.categoryName,
                                        style = TypographySaveGame.bodyLarge
                                    )
                                    Text(
                                        text = String.format(
                                            LocalLocale.current.platformLocale,
                                            "%.1f%%",
                                            percentual * 100
                                        ),
                                        style = TypographySaveGame.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            alpha = 0.7f
                                        )
                                    )
                                }
                            }
                            Text(
                                text = String.format(
                                    LocalLocale.current.platformLocale,
                                    stringResource(R.string.r_2f),
                                    category.total
                                ),
                                style = TypographySaveGame.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        LinearProgressIndicator(
                            progress = { percentual },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp),
                            color = labelColor,
                            trackColor = Color.Transparent
                        )
                    }
                }
            }
        }
        // --- EXPORT BUTTON ---
        item {
            Button(
                onClick = onExportClick,
                shape = customCardShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(48.dp)
            ) {
                Text(
                    stringResource(R.string.export_json_backup),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = TypographySaveGame.labelSmall
                )
            }
        }
    }
}
