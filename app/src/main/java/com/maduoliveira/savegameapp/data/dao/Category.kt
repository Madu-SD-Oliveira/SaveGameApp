package com.maduoliveira.savegameapp.data.dao

import androidx.room.*
import com.maduoliveira.savegameapp.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

/** Aqui foi criado um dao para a categoria para inserir e atualizar os informacoes e filtros de busca por tipo.*/

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category WHERE type = :type ORDER BY name ASC")
    fun getCategoryByType(type: String): Flow<List<CategoryEntity>>
    @Query("SELECT * FROM category ORDER BY name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)
    @Delete
    suspend fun deleteCategory(category: CategoryEntity)
}