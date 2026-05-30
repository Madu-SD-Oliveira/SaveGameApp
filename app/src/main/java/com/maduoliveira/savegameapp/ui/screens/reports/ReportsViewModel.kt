package com.maduoliveira.savegameapp.ui.screens.reports

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
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ReportsViewModel(
    private val repository: FinancesRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()

    init {
        uploadReportData()    }

    fun onPeriodSelect(period: String) {
        _uiState.update { it.copy(selectedPeriod = period) }
        uploadReportData()
    }

    fun onTypeSelect(type: String) {
        _uiState.update { it.copy(selectedType = type) }
        uploadReportData()
    }

    private fun uploadReportData() {
        val currentState = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val calendar = Calendar.getInstance()
            val fimTimestamp = calendar.timeInMillis

            val mesesSubtrair = if (currentState.selectedPeriod == "1 Month") -1 else -3
            calendar.add(Calendar.MONTH, mesesSubtrair)
            val inicioTimestamp = calendar.timeInMillis

            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            println("🔎 [LOGCAT FILTRO] Tipo buscado: '${currentState.selectedType}'")
            println("🔎 [LOGCAT FILTRO] De: ${sdf.format(Date(inicioTimestamp))} ($inicioTimestamp)")
            println("🔎 [LOGCAT FILTRO] Até: ${sdf.format(Date(fimTimestamp))} ($fimTimestamp)")

            // Busca do banco
            val joinedData = repository.getReportsByCategoryPeriod(
                type = currentState.selectedType,
                startDate = inicioTimestamp,
                endDate = fimTimestamp
            ).firstOrNull() ?: emptyList()

            println("🔎 [LOGCAT BANCO] Quantidade de linhas retornadas pelo Room: ${joinedData.size}")
            joinedData.forEach {
                println("   -> Categoria: ${it.categoryName} | Total: R$ ${it.total}")
            }

            val todasGerais = repository.getAllTransactions.firstOrNull() ?: emptyList()
            println("🔎 [LOGCAT GERAL] Total de transações existentes no banco inteiro: ${todasGerais.size}")
            if (todasGerais.isNotEmpty()) {
                println("   -> Amostra da última transação salva no banco:")
                val ultima = todasGerais.last()
                println("      Nome: ${ultima.name} | Tipo: ${ultima.type} | Valor: ${ultima.value} | Data no Banco: ${sdf.format(
                    Date(ultima.date)
                )}")
            }

            val totalGeral = joinedData.sumOf { it.total }

            _uiState.update { state ->
                state.copy(
                    category = joinedData,
                    totalPeriodo = totalGeral,
                    isLoading = false
                )
            }
        }
    }

    fun exportJsonData() {
        viewModelScope.launch {
            try {
                val allTransactions: List<Transaction> = repository.getAllTransactions.firstOrNull() ?: emptyList()
                val builder = StringBuilder()
                builder.append("[\n")
                allTransactions.forEachIndexed { index, t ->
                    builder.append("  {\n")
                    builder.append("    \"id\": ${t.id},\n")
                    builder.append("    \"name\": \"${t.name}\",\n")
                    builder.append("    \"value\": ${t.value},\n")
                    builder.append("    \"type\": \"${t.type}\",\n")
                    builder.append("    \"date\": ${t.date},\n")
                    builder.append("    \"description\": \"${t.description}\"\n")
                    builder.append("  }")
                    if (index < allTransactions.lastIndex) builder.append(",")
                    builder.append("\n")
                }
                builder.append("]")

                _uiState.update { it.copy(jsonExport = builder.toString()) }
            } catch (e: Exception) {
                _uiState.update { it.copy(jsonExport = "{ \"erro\": \"Falha ao gerar backup\" }") }
            }
        }
    }

    fun resetExportState() {
        _uiState.update { it.copy(jsonExport = null) }
    }
}
