package com.maduoliveira.savegameapp.data.repository

import com.maduoliveira.savegameapp.data.dao.AccountDao
import com.maduoliveira.savegameapp.data.dao.CategoryDao
import com.maduoliveira.savegameapp.data.dao.ChannelDao
import com.maduoliveira.savegameapp.data.dao.ReportsCategory
import com.maduoliveira.savegameapp.data.dao.ReportsDao
import com.maduoliveira.savegameapp.data.dao.SectorDao
import com.maduoliveira.savegameapp.data.dao.TransactionDao
import com.maduoliveira.savegameapp.domain.mapper.toDomain
import com.maduoliveira.savegameapp.domain.mapper.toEntity
import com.maduoliveira.savegameapp.domain.model.Account
import com.maduoliveira.savegameapp.domain.model.Category
import com.maduoliveira.savegameapp.domain.model.Channel
import com.maduoliveira.savegameapp.domain.model.ReportsCategoryDomain
import com.maduoliveira.savegameapp.domain.model.Sector
import com.maduoliveira.savegameapp.domain.model.Transaction
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

/*Aqui foi criado o repositorio global da aplicacão. Aonde trazemos os dao`s a um lugar só. Criamos acoes de dados e filtros. */
class FinancesRepository(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao,
    private val channelDao: ChannelDao,
    private val accountDao: AccountDao,
    private val reportsDao: ReportsDao,
    private val sectorDao: SectorDao
) {
    //  TRANSACTIONS

    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction.toEntity())
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction.toEntity())
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction.toEntity())
    }

    val getAllTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions().map { entities ->
        entities.map { it.toDomain() }
    }

    fun getTransactionsByType(type: String): Flow<List<Transaction>> =
        transactionDao.getTransactionByType(type).map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun getTransactionById(id: Long): Transaction? {
        return transactionDao.getTransactionById(id)?.toDomain()
    }

    // TELA 2
    fun searchTransactions(
        queryText: String,
        categoryId: Int?,
        startDate: Long,
        endDate: Long
    ): Flow<List<Transaction>> =
        transactionDao.searchTransactions(queryText, categoryId, startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }

    // REPORTS

    fun getReportsByCategoryPeriod(type: String, startDate: Long, endDate: Long): Flow<List<ReportsCategoryDomain>> =
        reportsDao.getReportsByCategoryPeriod(type, startDate, endDate)

    fun getAllReports(type: String): Flow<List<ReportsCategory>> =
        reportsDao.getAllReports(type)

    // CATEGORIES

    val getAllCategories: Flow<List<Category>> = categoryDao.getAllCategories().map { entities ->
        entities.map { it.toDomain() }
    }

    fun getCategoryByType(type: String): Flow<List<Category>> =
        categoryDao.getCategoryByType(type).map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun insertCategory(categoria: Category) =
        categoryDao.insertCategory(categoria.toEntity())

    // ACCOUNTS

    val allAccounts: Flow<List<Account>> = accountDao.getAllAccounts().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun getAccountById(id: Int): Account? =
        accountDao.getAccountById(id)?.toDomain()

    suspend fun insertAccount(account: Account) =
        accountDao.insertAccount(account.toEntity())

    suspend fun updateAccount(account: Account) =
        accountDao.updateAccount(account.toEntity())

    suspend fun deleteAccount(account: Account) =
        accountDao.deleteAccount(account.toEntity())

    // Channel

    suspend fun insertChannel(channel: Channel) =
        channelDao.insertChannel(channel.toEntity())
    val getAllChannels: Flow<List<Channel>> = channelDao.getAllChannels().map { entities ->
        entities.map { it.toDomain() }
    }
    //  SECTORS

    val getAllSectors: Flow<List<Sector>> = sectorDao.getAllSectors().map { entities ->
        entities.map { it.toDomain() }
    }
    

    suspend fun insertSector(sector: Sector) =
        sectorDao.insertSector(sector.toEntity())

    // EXPORT JSON

    fun exportDataJson(transactions: List<Transaction>): String {
        return Json.encodeToString(transactions)
    }
}