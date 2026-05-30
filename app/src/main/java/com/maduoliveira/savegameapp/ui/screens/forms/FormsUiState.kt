package com.maduoliveira.savegameapp.ui.screens.forms

data class FormsUiState(
    val name: String = "",
    val description: String = "",
    val value: String = "",
    val type: String = "INCOMES",
    val dateText: String = "",
    val categoryId: Int? = null,
    val accountId:  Int? = null,
    val sectorId:  Int? = null,
    val channelId: Int? = null,

    // Dropdowns
    val selectedCategory: String = "",
    val categoriesAvailable: List<String> = emptyList(),

    val selectedSector: String = "",
    val sectorsAvailable: List<String> = emptyList(),

    val selectedAccount: String = "",
    val accountsAvailable: List<String> = emptyList(),

    val selectedChannel: String = "",
    val channelsAvailable: List<String> = emptyList(),

    // Validações e Controle de Fluxo
    val valueError: String? = null,
    val dateError: String? = null,
    val isTransactionSaved: Boolean = false,
    val isLoading: Boolean = false
)
