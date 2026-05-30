package com.maduoliveira.savegameapp.domain.mapper

import com.maduoliveira.savegameapp.data.dao.ReportsCategory
import com.maduoliveira.savegameapp.data.entity.AccountEntity
import com.maduoliveira.savegameapp.data.entity.CategoryEntity
import com.maduoliveira.savegameapp.data.entity.ChannelEntity
import com.maduoliveira.savegameapp.data.entity.SectorEntity
import com.maduoliveira.savegameapp.data.entity.TransactionEntity
import com.maduoliveira.savegameapp.domain.model.Account
import com.maduoliveira.savegameapp.domain.model.Category
import com.maduoliveira.savegameapp.domain.model.Channel
import com.maduoliveira.savegameapp.domain.model.ReportsCategoryDomain
import com.maduoliveira.savegameapp.domain.model.Sector
import com.maduoliveira.savegameapp.domain.model.Transaction

//  ACCOUNT MAPPERS

fun AccountEntity.toDomain() = Account(
    id = id,
    name = name
)

fun Account.toEntity() = AccountEntity(
    id = id,
    name = name
)

// CATEGORY MAPPERS

fun CategoryEntity.toDomain() = Category(
    id = id,
    name = name,
    type = type,
    icon = icon
)

fun Category.toEntity() = CategoryEntity(
    id = id,
    name = name,
    type = type,
    icon = icon
)
// CHANNEL
fun ChannelEntity.toDomain() = Channel(
    id = id,
    name = name,
    type = type,
    icon = icon
)

fun Channel.toEntity() = ChannelEntity(
    id = id,
    name = name,
    type = type,
    icon = icon
)

//  SECTOR MAPPERS

fun SectorEntity.toDomain() = Sector(
    id = id,
    name = name,
)

fun Sector.toEntity() = SectorEntity(
    id = id,
    name = name
)

// TRANSACTION MAPPERS

fun TransactionEntity.toDomain() = Transaction(
    id = id,
    name = name,
    value = value,
    type = type,
    date = date,
    categoryId = categoryId,
    accountId = accountId,
    channelId = channelId,
    sectorId = sectorId,
    description = description
)

fun Transaction.toEntity() = TransactionEntity(
    id = id,
    name = name,
    value = value,
    type = type,
    date = date,
    categoryId = categoryId,
    accountId = accountId,
    sectorId = sectorId,
    channelId = channelId,
    description = description
)

//  REPORTS MAPPER

fun ReportsCategory.toDomain() = ReportsCategoryDomain(
    categoryName = categoryName,
    total = total
)