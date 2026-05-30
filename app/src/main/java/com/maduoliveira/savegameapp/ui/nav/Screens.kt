package com.maduoliveira.savegameapp.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

object Screens {
    const val SPLASH = "splash"
    const val DASHBOARD = "dashboard"
    const val TRANSACTIONS = "transactions"
    const val FORM = "form/{id}"
    const val REPORTS = "reports"
    const val SETTINGS = "settings"
    fun form(id: Long = -1L) = "form/$id"
}

// To help on the bottom bar
sealed class BottomBarScreen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : BottomBarScreen(Screens.DASHBOARD, "Home", Icons.Default.Home)
    object Transactions : BottomBarScreen(Screens.TRANSACTIONS, "Transaction", Icons.Default.List)
    object Form : BottomBarScreen(Screens.form(-1L), "Add", Icons.Default.Add)
    object Reports : BottomBarScreen(Screens.REPORTS, "Graphs", Icons.Default.DateRange)
    object Settings : BottomBarScreen(Screens.SETTINGS, "Settings", Icons.Default.Settings)
}