package com.maduoliveira.savegameapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Int = 0,
    val name: String,
    val type: String,
    val icon: Int
)