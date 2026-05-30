package com.maduoliveira.savegameapp.ui.screens.transaction

import com.maduoliveira.savegameapp.domain.model.Transaction

data class TransactionUIModel(
    val transaction: Transaction,
    val categoryName: String = "",
    val categoryIcon: Int? = null,
    val channelName: String = "",
    val channelIcon: Int? = null,
    val sectorName: String = "",
    val accountName: String = ""
)

data class TransactionsUiState(
    val transactions: List<TransactionUIModel> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val isSearchExpanded: Boolean = false,
    val selectedPeriod: String = "All", // "All", "1 Month", "3 Month"
    val selectedType: String = "ALL",     // "ALL", "INCOMES", "EXPENSES"
    val selectedSector: String = "All",
    val availableSectors: List<String> = emptyList(),
    val selectedCategory: String = "All",
    val availableCategories: List<String> = emptyList(),
    val selectedChannel: String = "All",
    val availableChannels: List<String> = emptyList(),
    val isAscending: Boolean = false
)
