package com.maduoliveira.savegameapp.data.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.maduoliveira.savegameapp.data.entity.SectorEntity
import kotlinx.coroutines.flow.Flow

/**Aqui está o Dao para o setor onde podemos filtrar por tipo, exibir todos e criar.*/

@Dao
interface SectorDao {
    @Query("SELECT * FROM sector ORDER BY name ASC")
    fun getAllSectors(): Flow<List<SectorEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSector(sector: SectorEntity)
    @Update
    suspend fun updateSector(sector: SectorEntity)
    @Delete
    suspend fun deleteSector(sector: SectorEntity)
}