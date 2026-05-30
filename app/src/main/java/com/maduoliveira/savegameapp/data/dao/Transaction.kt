package com.maduoliveira.savegameapp.data.dao

import androidx.room.*
import com.maduoliveira.savegameapp.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)
    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)
    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)
    @Query("SELECT * FROM `transaction` WHERE id = :id")
    suspend fun getTransactionById(id: Long): TransactionEntity?
    @Query("SELECT * FROM `transaction` ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>
    @Query("SELECT * FROM `transaction` WHERE type = :type ORDER BY date DESC")
    fun getTransactionByType(type: String): Flow<List<TransactionEntity>>
    @Query("SELECT * FROM `transaction` WHERE sectorId = :sectorId ORDER BY date DESC")
    fun getTransactionBySector(sectorId: Int): Flow<List<TransactionEntity>>
    @Query("""
        SELECT * FROM `transaction` 
        WHERE (:categoryId IS NULL OR categoryId = :categoryId)
        AND (name LIKE '%' || :queryText || '%' OR description LIKE '%' || :queryText || '%')
        AND (date BETWEEN :startDate AND :endDate)
        ORDER BY date DESC
    """)
    fun searchTransactions(
        queryText: String,
        categoryId: Int?,
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionEntity>>
}