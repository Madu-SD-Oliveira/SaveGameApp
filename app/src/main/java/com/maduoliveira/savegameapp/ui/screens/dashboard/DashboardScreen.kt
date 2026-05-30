package com.maduoliveira.savegameapp.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.maduoliveira.savegameapp.R
import com.maduoliveira.savegameapp.domain.dashboard.DashboardUiState
import com.maduoliveira.savegameapp.ui.nav.Screens
import com.maduoliveira.savegameapp.ui.theme.SaveGameAppTheme
import com.maduoliveira.savegameapp.ui.theme.TypographySaveGame

@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onToggleBalanceVisibility: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var showLevelDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            item(key = "header_dashboard") {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.dashboard),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = TypographySaveGame.headlineMedium
                        )

                        TextButton(
                            onClick = { showLevelDialog = true },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.tertiary
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.level, uiState.currentLevel),
                                modifier = Modifier,
                                textAlign = TextAlign.Center,
                                style = TypographySaveGame.labelSmall
                            )
                        }
                    }
                }
            }
            item(key = "card_balance") {
                CardBalance(
                    balance = uiState.totalBalance,
                    isBalanceVisible = uiState.isBalanceVisible,
                    percentageChange = uiState.percentageChange,
                    onToggleVisibility = onToggleBalanceVisibility,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item(key = "cards_info") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CardInfo(
                        title = stringResource(R.string.incomes),
                        value = uiState.totalIncomes,
                        isIncome = true,
                        isBalanceVisible = uiState.isBalanceVisible,
                        percentageChange = uiState.percentIncomesChange,
                        modifier = Modifier.fillMaxWidth()
                    )
                    CardInfo(
                        title = stringResource(R.string.expenses),
                        value = uiState.totalExpenses,
                        isIncome = false,
                        isBalanceVisible = uiState.isBalanceVisible,
                        percentageChange = uiState.percentageExpensesChange,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            item(key = "card_recent_transactions") {
                CardRecents(
                    transactions = uiState.recentTransactions,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item("Badges Row") {
                BadgesSection(
                    badges = uiState.badges,
                    onSeeAllClick = { navController.navigate(Screens.SETTINGS)}
                )
            }

           item ("spacer"){ Spacer( Modifier.size(150.dp))}
        }
    }

    if (showLevelDialog) {
        AlertDialog(
            onDismissRequest = { showLevelDialog = false },
            confirmButton = {
                TextButton(onClick = { showLevelDialog = false }) {
                    Text(stringResource(R.string.got_it))
                }
            },containerColor = MaterialTheme.colorScheme.surface,

            title = {
                Text(
                    text = stringResource(
                        id = R.string.level_title,
                        uiState.currentLevel,
                        uiState.levelName
                    ),
                    style = TypographySaveGame.titleLarge
                )
            },
            text = {
                Text(
                    text = stringResource(
                        id = R.string.level_description,
                        uiState.currentXp
                    ),
                    style = TypographySaveGame.bodyMedium
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    SaveGameAppTheme {
        DashboardScreen(
            uiState = DashboardUiState(
                totalBalance = 1234.56,
                totalIncomes = 2000.0,
                totalExpenses = 765.44,
                percentageChange = 12.5,
                recentTransactions = listOf(
                ),
                percentIncomesChange = 0.0,
                percentageExpensesChange = -4.2,
                currentLevel = 2,
                levelName = "SAVER",
                currentXp = 200,
                xpNeededForNextLevel = 800,
                recentAchievement = "Primeiro depósito",
                isBalanceVisible = true,
                isLoading = false,
            ),
            onToggleBalanceVisibility = {},
            navController = rememberNavController()
        )
    }
}