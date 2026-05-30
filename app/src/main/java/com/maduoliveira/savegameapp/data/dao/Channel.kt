package com.maduoliveira.savegameapp.data.dao

import androidx.room.*
import com.maduoliveira.savegameapp.data.entity.CategoryEntity
import com.maduoliveira.savegameapp.data.entity.ChannelEntity
import kotlinx.coroutines.flow.Flow

/** Aqui foi criado um dao para a categoria para inserir e atualizar os informacoes e filtros de busca por tipo.*/

@Dao
interface ChannelDao {
    @Query("SELECT * FROM channel WHERE type = :type ORDER BY name ASC")
    fun getChannelByType(type: String): Flow<List<ChannelEntity>>
    @Query("SELECT * FROM channel ORDER BY name ASC")
    fun getAllChannels(): Flow<List<ChannelEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannel(channel: ChannelEntity)
    @Delete
    suspend fun deleteChannel(channel: ChannelEntity)
}