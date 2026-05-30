package com.maduoliveira.savegameapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


/** Foi criada uma entidade para a classe conta, categoria e setor.
 A conta guarda o Name e Id do input.
 E salva se a entrada é da categoria: receita ou um saldo.
 Também salva se a transacao é de qual setor (mercado, shopping,etc, gastos fixos...)

Logo estrutura no banco de dados sobre o nome "transaction" as informacoes conjuntas com seu index.
Posteriormente cria uma classe Transaction : Guardamos como Timestamp (Long) para facilitar filtros.
Contendo a informacao completa da entradas da tela New Transaction */

@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val type: String, // "INCOMES" ou "EXPENSES"
    val icon: Int
)

@Entity(tableName = "channel")
data class ChannelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val type: String,
    val icon: Int
)

@Entity(tableName = "sector")
data class SectorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
)

@Entity(
    tableName = "transaction",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ChannelEntity::class,
            parentColumns = ["id"],
            childColumns = ["channelId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SectorEntity::class,
            parentColumns = ["id"],
            childColumns = ["sectorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("accountId"), Index("categoryId"), Index("channelId"), Index("sectorId")]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val description: String = "",
    val name: String = "",
    val value: Double = 0.0,
    val type: String = "",
    val date: Long = 0L,
    val categoryId: Int?,
    val accountId:Int?,
    val channelId: Int?,
    val sectorId: Int?
)