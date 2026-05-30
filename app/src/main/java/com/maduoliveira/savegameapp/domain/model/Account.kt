package com.maduoliveira.savegameapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: Int = 0,
    val name: String
)