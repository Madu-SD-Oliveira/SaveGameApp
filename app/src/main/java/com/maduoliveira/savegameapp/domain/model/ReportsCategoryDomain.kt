package com.maduoliveira.savegameapp.domain.model
import kotlinx.serialization.Serializable

@Serializable
data class  ReportsCategoryDomain (
    val categoryName: String,
    val total: Double
)