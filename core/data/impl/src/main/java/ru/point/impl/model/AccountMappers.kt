package ru.point.impl.model

import java.time.Instant
import ru.point.api.model.AccountDto
import ru.point.local.entities.AccountEntity

fun AccountDto.toAccountEntity(isSynced: Boolean) = AccountEntity(
    id = id,
    userId = userId,
    name = name,
    balance = balance,
    currency = currency,
    createdAt = createdAt,
    updatedAt = updatedAt,
    updatedAtMillis = Instant.parse(updatedAt).toEpochMilli(),
    isSynced = isSynced
)

fun AccountEntity.toAccountDto() = AccountDto(
    id,
    userId,
    name,
    balance,
    currency,
    createdAt,
    updatedAt
)

fun Account.accountToAccountDto() = AccountDto(
    id = id,
    userId = userId,
    name = name,
    balance = balance,
    currency = currency,
    createdAt = createdAt,
    updatedAt = updatedAt,
)