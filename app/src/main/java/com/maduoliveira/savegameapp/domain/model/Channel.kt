package com.maduoliveira.savegameapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Channel(
    val id: Int = 0,
    val name: String,
    val type: String,
    val icon: Int
)