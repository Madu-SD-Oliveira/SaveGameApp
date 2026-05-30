package com.maduoliveira.savegameapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Sector(
    val id: Int = 0,
    val name: String,
)