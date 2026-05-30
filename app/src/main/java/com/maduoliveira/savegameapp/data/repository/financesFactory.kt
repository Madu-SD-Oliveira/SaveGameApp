package com.maduoliveira.savegameapp.data.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maduoliveira.savegameapp.ui.screens.dashboard.DashboardViewModel
import com.maduoliveira.savegameapp.ui.screens.forms.FormsViewModel
import com.maduoliveira.savegameapp.ui.screens.reports.ReportsViewModel
import com.maduoliveira.savegameapp.ui.screens.settings.SettingsViewModel
import com.maduoliveira.savegameapp.ui.screens.transaction.TransactionsViewModel

fun financesFactory(repository: FinancesRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FormsViewModel::class.java) -> {
                FormsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(TransactionsViewModel::class.java) -> {
                TransactionsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ReportsViewModel::class.java) -> {
                ReportsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }
}