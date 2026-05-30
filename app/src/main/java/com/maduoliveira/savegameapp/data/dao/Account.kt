package com.maduoliveira.savegameapp.data.dao
import androidx.room.*
import com.maduoliveira.savegameapp.data.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

/** Aqui foi criado um dao para a conta para inserir e atualizar os informacoes e filtros de busca global e por id.*/
@Dao
interface AccountDao {
    @Query("SELECT * FROM account ORDER BY name ASC")
    fun getAllAccounts(): Flow<List<AccountEntity>>
    @Query("SELECT * FROM account WHERE id = :id")
    suspend fun getAccountById(id: Int): AccountEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity)
    @Update
    suspend fun updateAccount(account: AccountEntity)
    @Delete
    suspend fun deleteAccount(account: AccountEntity)
}