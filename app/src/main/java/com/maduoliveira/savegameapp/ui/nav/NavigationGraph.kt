package com.maduoliveira.savegameapp.ui.nav

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.maduoliveira.savegameapp.R
import com.maduoliveira.savegameapp.SaveGameApplication
import com.maduoliveira.savegameapp.data.repository.financesFactory
import com.maduoliveira.savegameapp.ui.screens.dashboard.DashboardViewModel
import com.maduoliveira.savegameapp.ui.screens.forms.FormsViewModel
import com.maduoliveira.savegameapp.ui.screens.reports.ReportsViewModel
import com.maduoliveira.savegameapp.ui.screens.settings.SettingsViewModel
import com.maduoliveira.savegameapp.ui.screens.transaction.TransactionsViewModel
import com.maduoliveira.savegameapp.ui.screens.FormsScreen
import com.maduoliveira.savegameapp.ui.screens.reports.ReportsScreen
import com.maduoliveira.savegameapp.ui.screens.dashboard.DashboardScreen
import com.maduoliveira.savegameapp.ui.screens.SaveGameSplashScreen
import com.maduoliveira.savegameapp.ui.screens.settings.SettingsScreen
import com.maduoliveira.savegameapp.ui.screens.transaction.TransactionsScreen

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun NavigationGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    onThemeChange: (Boolean) -> Unit,
    isDark: Boolean,

) {
    NavHost(
        navController = navController,
        startDestination = Screens.SPLASH,
        modifier = Modifier.padding(innerPadding)
    ) {
        //  Tela Splash
        composable(Screens.SPLASH) {
            SaveGameSplashScreen(
                onTargetReached =
                    {
                        navController.navigate(Screens.DASHBOARD)
                        {
                            popUpTo(Screens.SPLASH)
                            { inclusive = true }
                        }
                    }
            )
        }

        // Tela Dashboard
        composable(Screens.DASHBOARD) {
            val repository =
                (LocalContext.current.applicationContext as SaveGameApplication).financesRepository
            val dashboardViewModel: DashboardViewModel = viewModel(
                factory = financesFactory(repository)
            )
            val uiState by dashboardViewModel.uiState.collectAsStateWithLifecycle()
            DashboardScreen(
                uiState = uiState,
                onToggleBalanceVisibility = { dashboardViewModel.toggleBalanceVisibility() },
                modifier = Modifier.fillMaxSize(),
                navController = navController
            )
        }
        // Tela Transaction List
        composable(Screens.TRANSACTIONS) {
            val repository =
                (LocalContext.current.applicationContext as SaveGameApplication).financesRepository
            val transactionsViewModel: TransactionsViewModel = viewModel(
                factory = financesFactory(repository)
            )
            TransactionsScreen(
                viewModel = transactionsViewModel,
                onEditTransaction = { transactionId ->
                    navController.navigate(Screens.form(id = transactionId))
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Tela Forms

        composable(
            route = Screens.FORM,
            arguments = listOf(navArgument("id") { type = NavType.LongType; defaultValue = -1L })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: -1L
            val repository =
                (LocalContext.current.applicationContext as SaveGameApplication).financesRepository
            val formsViewModel: FormsViewModel = viewModel(
                factory = financesFactory(repository)
            )

            LaunchedEffect(id) {
                if (id != -1L) {
                    formsViewModel.loadTransaction(id)
                } else {
                    formsViewModel.clearForm()
                }
            }

            val state by formsViewModel.uiState.collectAsStateWithLifecycle()
            FormsScreen(
                uiState = state,
                onDescriptionChange = { formsViewModel.onDescriptionChange(it) },
                onNameChange = { formsViewModel.onNameChange(it)},
                onValueChange = { formsViewModel.onValueChange(it) },
                onTypeChange = { formsViewModel.onTypeChange(it) },
                onDateChange = { formsViewModel.onDateChange(it) },
                onCategorySelect = { formsViewModel.onCategorySelect(it) },
                onSectorSelect = { formsViewModel.onSectorSelect(it) },
                onChannelSelect = { formsViewModel.onChannelSelect(it)},
                onAccountSelect = { formsViewModel.onAccountSelect(it) },
                onSaveClick = { formsViewModel.saveTransaction() },
                onBackClick = { navController.popBackStack() }
            )
        }

        // Relatórios
        composable(Screens.REPORTS) {
            val repository =
                (LocalContext.current.applicationContext as SaveGameApplication).financesRepository
            val resumoViewModel: ReportsViewModel = viewModel(
                factory = financesFactory(repository)
            )
            val uiState by resumoViewModel.uiState.collectAsStateWithLifecycle()

            ReportsScreen(
                uiState = uiState,
                onPeriodSelect = { resumoViewModel.onPeriodSelect(it) },
                onTypeSelect = { resumoViewModel.onTypeSelect(it) },
                onExportClick = { resumoViewModel.exportJsonData() },
                onExportHandled = { resumoViewModel.resetExportState() },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Settings
        composable(Screens.SETTINGS) {
            val repository =
                (LocalContext.current.applicationContext as SaveGameApplication).financesRepository
            val viewModel: SettingsViewModel = viewModel(
                factory = financesFactory(repository)
            )
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val context = LocalContext.current

            SettingsScreen(
                uiState = uiState,
                onThemeToggle = { isDark ->
                    viewModel.toggleTheme(isDark)
                    onThemeChange(isDark)
                },
                onBadgeClick = { badge ->
                    if (badge.isUnlocked) {
                        viewModel.triggerConfetti()
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.blocked),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                onConfettiFinished = {
                    viewModel.resetConfetti()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}