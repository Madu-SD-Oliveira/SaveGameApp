package com.maduoliveira.savegameapp.ui.screens.forms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maduoliveira.savegameapp.data.repository.FinancesRepository
import com.maduoliveira.savegameapp.domain.model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormsViewModel(
    private val repository: FinancesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FormsUiState())
    val uiState: StateFlow<FormsUiState> = _uiState.asStateFlow()
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private var currentTransactionId: Long = -1L

    init {
        val today = dateFormatter.format(Date())
        _uiState.update { it.copy(dateText = today) }
        loadDropdownData()
    }

    private fun loadDropdownData() {
        viewModelScope.launch {
            repository.getAllCategories.collect { categories ->
                _uiState.update { it.copy(categoriesAvailable = categories.map { it.name }) }
            }
        }
        viewModelScope.launch {
            repository.getAllSectors.collect { sectors ->
                _uiState.update { it.copy(sectorsAvailable = sectors.map { it.name }) }
            }
        }
        viewModelScope.launch {
            repository.allAccounts.collect { accounts ->
                _uiState.update { it.copy(accountsAvailable = accounts.map { it.name }) }
            }
        }
        viewModelScope.launch {
            repository.getAllChannels.collect { channels ->
                _uiState.update { it.copy(channelsAvailable = channels.map { it.name }) }
            }
        }
    }

    fun onDateChange(newDateText: String) {
        _uiState.update { it.copy(dateText = newDateText, dateError = null) }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { it.copy(description = newDescription) }
    }
    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onValueChange(newValue: String) {
        if (newValue.all { it.isDigit() || it == '.' || it == ',' } || newValue.isEmpty()) {
            _uiState.update { it.copy(value = newValue, valueError = null) }
        }
    }

    fun onTypeChange(newType: String) {
        _uiState.update { it.copy(type = newType) }
    }

    fun onCategorySelect(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onSectorSelect(sector: String) {
        _uiState.update { it.copy(selectedSector = sector) }
    }
    fun onChannelSelect(channel: String) {
        _uiState.update { it.copy(selectedChannel = channel) }
    }


    fun onAccountSelect(account: String) {
        _uiState.update { it.copy(selectedAccount = account) }
    }

    fun loadTransaction(id: Long) {
        currentTransactionId = id
        viewModelScope.launch {
            val transaction = repository.getTransactionById(id)
            transaction?.let {
                val formattedDate = dateFormatter.format(Date(it.date))
                
                // Buscar nomes correspondentes aos IDs
                val categories = repository.getAllCategories.firstOrNull() ?: emptyList()
                val sectors = repository.getAllSectors.firstOrNull() ?: emptyList()
                val accounts = repository.allAccounts.firstOrNull() ?: emptyList()
                val channels = repository.getAllChannels.firstOrNull() ?: emptyList()

                val categoryName = categories.find { c -> c.id == it.categoryId }?.name ?: ""
                val sectorName = sectors.find { s -> s.id == it.sectorId }?.name ?: ""
                val accountName = accounts.find { a -> a.id == it.accountId }?.name ?: ""
                val channelName = channels.find { ch -> ch.id == it.channelId }?.name ?: ""

                _uiState.update { state ->
                    state.copy(
                        name = it.name,
                        description = it.description,
                        value = it.value.toString(),
                        type = it.type,
                        dateText = formattedDate,
                        categoryId = it.categoryId,
                        accountId = it.accountId,
                        sectorId = it.sectorId,
                        channelId = it.channelId,
                        selectedCategory = categoryName,
                        selectedSector = sectorName,
                        selectedAccount = accountName,
                        selectedChannel = channelName
                    )
                }
            }
        }
    }
    fun clearForm() {
        currentTransactionId = -1L
        val today = dateFormatter.format(Date())
        _uiState.update { 
            FormsUiState(
                dateText = today,
                categoriesAvailable = it.categoriesAvailable,
                sectorsAvailable = it.sectorsAvailable,
                accountsAvailable = it.accountsAvailable,
                channelsAvailable = it.channelsAvailable
            )
        }
    }
    fun saveTransaction() {
        val currentState = _uiState.value

        val parsedValue = currentState.value.replace(",", ".").toDoubleOrNull() ?: 0.0
        val isValueInvalid = parsedValue <= 0.0

        val timestamp = try {
            dateFormatter.isLenient = false
            dateFormatter.parse(currentState.dateText)?.time
        } catch (e: Exception) {
            null
        }

        if (isValueInvalid || timestamp == null) {
            _uiState.update { state ->
                state.copy(
                    valueError = if (isValueInvalid) "O valor deve ser maior que zero" else null,
                    dateError = if (timestamp == null) "Data inválida. Use DD/MM/AAAA" else null
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val listaCategorias = repository.getAllCategories.firstOrNull() ?: emptyList()
                val listaContas = repository.allAccounts.firstOrNull() ?: emptyList()
                val listaSetores = repository.getAllSectors.firstOrNull() ?: emptyList()
                val listaCanais = repository.getAllChannels.firstOrNull() ?: emptyList()


                val idCategoriaEncontrado = listaCategorias.find { it.name == currentState.selectedCategory }?.id
                val idContaEncontrado = listaContas.find { it.name == currentState.selectedAccount }?.id
                val idSetorEncontrado = listaSetores.find { it.name == currentState.selectedSector }?.id
                val idSCanaisEncontrado = listaCanais.find { it.name == currentState.selectedChannel }?.id


                val newTransaction = Transaction(
                    id = if (currentTransactionId == -1L) 0L else currentTransactionId,
                    description = currentState.description.ifBlank { "Sem descrição" },
                    name = currentState.name.ifBlank { 
                        if (currentState.type == "INCOMES") currentState.selectedChannel else currentState.selectedCategory 
                    }.ifBlank { "Nova Transação" },
                    value = parsedValue,
                    type = currentState.type,
                    date = timestamp,
                    categoryId = if (currentState.type == "EXPENSES") idCategoriaEncontrado else null,
                    accountId = idContaEncontrado,
                    channelId =  if (currentState.type == "INCOMES") idSCanaisEncontrado else null,
                    sectorId = idSetorEncontrado
                )

                repository.insertTransaction(newTransaction)
                _uiState.update { it.copy(isTransactionSaved = true, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}