package com.maduoliveira.savegameapp.ui.screens.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maduoliveira.savegameapp.data.repository.FinancesRepository
import com.maduoliveira.savegameapp.domain.model.Account
import com.maduoliveira.savegameapp.domain.model.Category
import com.maduoliveira.savegameapp.domain.model.Channel
import com.maduoliveira.savegameapp.domain.model.Sector
import com.maduoliveira.savegameapp.domain.model.Transaction
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

class TransactionsViewModel(
    private val repository: FinancesRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _isSearchExpanded = MutableStateFlow(false)
    private val _selectedPeriod = MutableStateFlow("All")
    private val _selectedType = MutableStateFlow("ALL")
    private val _selectedSector = MutableStateFlow("All")
    private val _selectedCategory = MutableStateFlow("All")
    private val _selectedChannel = MutableStateFlow("All")
    private val _isAscending = MutableStateFlow(false)

    val uiState: StateFlow<TransactionsUiState> = combine(
        repository.getAllTransactions,
        repository.getAllSectors,
        repository.getAllCategories,
        repository.getAllChannels,
        repository.allAccounts,
        _searchQuery,
        _isSearchExpanded,
        _selectedPeriod,
        _selectedType,
        _selectedSector,
        _selectedCategory,
        _selectedChannel,
        _isAscending
    ) { flowValues ->
        val allTransactions = flowValues[0] as List<Transaction>
        val allSectors = flowValues[1] as List<Sector>
        val allCategories = flowValues[2] as List<Category>
        val allChannels = flowValues[3] as List<Channel>
        val allAccounts = flowValues[4] as List<Account>
        val query = flowValues[5] as String
        val isSearchExpanded = flowValues[6] as Boolean
        val period = flowValues[7] as String
        val type = flowValues[8] as String
        val sectorName = flowValues[9] as String
        val categoryNameFilter = flowValues[10] as String
        val channelNameFilter = flowValues[11] as String
        val ascending = flowValues[12] as Boolean

        var filteredList = allTransactions

        // 1. Filtro por Texto
        if (query.isNotBlank()) {
            filteredList = filteredList.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
            }
        }

        // 2. Filtro por Tipo
        if (type != "ALL") {
            filteredList = filteredList.filter { it.type == type }
        }

        // 3. Filtro por Período
        if (period != "All") {
            val limitCalendar = Calendar.getInstance()
            val monthsToSubtract = if (period == "1 Month") -1 else -3
            limitCalendar.add(Calendar.MONTH, monthsToSubtract)
            val limitTimestamp = limitCalendar.timeInMillis
            filteredList = filteredList.filter { it.date >= limitTimestamp }
        }

        // 4. Filtro por Setor
        if (sectorName != "All") {
            val sectorId = allSectors.find { it.name == sectorName }?.id
            if (sectorId != null) {
                filteredList = filteredList.filter { it.sectorId == sectorId }
            }
        }

        // 5. Filtro por Categoria
        if (categoryNameFilter != "All") {
            val categoryId = allCategories.find { it.name == categoryNameFilter }?.id
            if (categoryId != null) {
                filteredList = filteredList.filter { it.categoryId == categoryId }
            }
        }

        // 6. Filtro por Canal
        if (channelNameFilter != "All") {
            val channelId = allChannels.find { it.name == channelNameFilter }?.id
            if (channelId != null) {
                filteredList = filteredList.filter { it.channelId == channelId }
            }
        }

        // 7. Ordenação
        filteredList = if (ascending) {
            filteredList.sortedBy { it.date }
        } else {
            filteredList.sortedByDescending { it.date }
        }

        // Map to UI Model
        val transactionsUI = filteredList.map { tx ->
            val cat = allCategories.find { it.id == tx.categoryId }
            val chan = allChannels.find { it.id == tx.channelId }
            val sect = allSectors.find { it.id == tx.sectorId }
            val acc = allAccounts.find { it.id == tx.accountId }

            TransactionUIModel(
                transaction = tx,
                categoryName = cat?.name ?: "",
                categoryIcon = cat?.icon,
                channelName = chan?.name ?: "",
                channelIcon = chan?.icon,
                sectorName = sect?.name ?: "",
                accountName = acc?.name ?: ""
            )
        }

        TransactionsUiState(
            transactions = transactionsUI,
            searchQuery = query,
            isSearchExpanded = isSearchExpanded,
            selectedPeriod = period,
            selectedType = type,
            selectedSector = sectorName,
            availableSectors = allSectors.map { it.name },
            selectedCategory = categoryNameFilter,
            availableCategories = allCategories.map { it.name },
            selectedChannel = channelNameFilter,
            availableChannels = allChannels.map { it.name },
            isAscending = ascending,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TransactionsUiState(isLoading = true)
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onSearchExpandedChange(expanded: Boolean) {
        _isSearchExpanded.value = expanded
        if (!expanded) _searchQuery.value = ""
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }
    fun onPeriodSelect(period: String) {
        _selectedPeriod.value = period
    }

    fun onTypeSelect(type: String) {
        _selectedType.value = type
    }

    fun onSectorSelect(sector: String) {
        _selectedSector.value = sector
    }

    fun onCategorySelect(category: String) {
        _selectedCategory.value = category
    }

    fun onChannelSelect(channel: String) {
        _selectedChannel.value = channel
    }

    fun toggleOrder() {
        _isAscending.value = !_isAscending.value
    }
}
