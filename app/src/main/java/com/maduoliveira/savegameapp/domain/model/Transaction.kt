package com.maduoliveira.savegameapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val id: Long = 0,
    val name: String, // Title
    val value: Double,
    val type: String, // "INCOMES" ou "EXPENSES"
    val date: Long,
    val categoryId: Int?, // Category
    val accountId: Int?,
    val channelId: Int?,
    val sectorId: Int?, // Sector Input Type
    val description: String = ""
)