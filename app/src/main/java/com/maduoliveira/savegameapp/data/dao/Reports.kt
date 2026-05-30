package com.maduoliveira.savegameapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.maduoliveira.savegameapp.domain.model.ReportsCategoryDomain
import kotlinx.coroutines.flow.Flow
@Dao
interface ReportsDao {
    @Query("""
        SELECT 
            COALESCE(c.name, t.name) as categoryName, 
            SUM(t.value) as total 
        FROM `transaction` t 
        LEFT JOIN category c ON t.categoryId = c.id 
        WHERE t.type = :type AND t.date BETWEEN :startDate AND :endDate
        GROUP BY t.name
    """)
    fun getReportsByCategoryPeriod(
        type: String,
        startDate: Long,
        endDate: Long
    ): Flow<List<ReportsCategoryDomain>>
    @Query("""
        SELECT c.name as categoryName, SUM(t.value) as total 
        FROM `transaction` t 
        INNER JOIN category c ON t.categoryId = c.id 
        WHERE t.type = :type
        GROUP BY c.id
    """)
    fun getAllReports(type: String): Flow<List<ReportsCategory>>
}
data class ReportsCategory(
    val categoryName: String,
    val total: Double
)