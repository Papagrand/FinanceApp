package ru.point.impl.repository

import java.math.BigDecimal
import java.time.Instant
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.point.api.flow.AccountPreferencesRepo
import ru.point.api.model.AccountDto
import ru.point.api.repository.AccountRepository
import ru.point.impl.flow.safeApiFlow
import ru.point.impl.model.Account
import ru.point.impl.model.AccountUpdateRequest
import ru.point.impl.model.accountToAccountDto
import ru.point.impl.model.toAccountDto
import ru.point.impl.model.toAccountEntity
import ru.point.impl.service.AccountService
import ru.point.local.dao.AccountDao
import ru.point.local.dao.TransactionDao
import ru.point.local.entities.TransactionEntity
import ru.point.utils.common.Result
import ru.point.utils.common.Result.Error
import ru.point.utils.common.Result.Loading
import ru.point.utils.common.Result.Success
import ru.point.utils.model.toAppError
import ru.point.utils.network.NetworkTracker

/**
 * AccountRepositoryImpl
 *
 * Ответственность:
 * - обращаться к REST API через Retrofit для получения данных аккаунта;
 * - преобразовывать DTO в доменную модель Account;
 * - оборачивать результаты в поток Flow<Result<Account>> через safeApiFlow.
 */

class AccountRepositoryImpl @Inject constructor(
    private val api: AccountService,
    private val dao: AccountDao,
    private val networkTracker: NetworkTracker,
    private val transactionDao: TransactionDao,
    private val prefs: AccountPreferencesRepo,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AccountRepository {

    override fun observe(): Flow<Result<AccountDto>> = channelFlow {
        send(Loading)
        dao.observe().collect { entity ->
            entity?.let { send(Success(it.toAccountDto())) }
        }
        launch(dispatcher) {
            if (networkTracker.online.first()) refreshFromRemote()
        }
    }.catch { e -> emit(Error(e.toAppError())) }
        .flowOn(dispatcher)


    override fun updateAccount(
        id: Int,
        name: String,
        balance: String,
        currency: String,
    ): Flow<Result<AccountDto>> = channelFlow {
        send(Loading)

        val local = dao.observe().first() ?: error("Account not cached")
        val now = System.currentTimeMillis()

        val updated = local.copy(
            name = name,
            balance = balance,
            currency = currency,
            updatedAt = Instant.ofEpochMilli(now).toString(),
            updatedAtMillis = now,
            isSynced = false
        )
        dao.upsert(updated)

        launch(dispatcher) {
            recalculateTransactionTotals(id, balance)
        }

        send(Success(updated.toAccountDto()))

        if (networkTracker.online.first()) {
            safeApiFlow {
                api.updateAccount(id, AccountUpdateRequest(name, balance, currency))
            }.collect { res ->
                when (res) {
                    is Loading -> {}
                    is Error -> send(Error(res.cause))
                    is Success -> {
                        dao.markSynced(
                            id,
                            res.data.updatedAt,
                            Instant.parse(res.data.updatedAt).toEpochMilli()
                        )
                        send(Success(res.data.accountToAccountDto()))
                    }
                }
            }
        }
    }.flowOn(dispatcher)

    override suspend fun refreshFromRemote() {
        safeApiFlow { api.getAccounts() }
            .firstOrNull { it is Success<*> }
            ?.let { result ->
                val list = (result as Success<List<Account>>).data
                val entity = list.first().accountToAccountDto().toAccountEntity(isSynced = true)
                dao.upsert(entity)
            }
    }

    private suspend fun recalculateTransactionTotals(
        accountId: Int,
        balanceStr: String
    ) {
        var currentBalance = balanceStr.toBigDecimalOrNull() ?: BigDecimal.ZERO

        val txs = transactionDao.getAllByAccountDesc(accountId)

        val updated = mutableListOf<TransactionEntity>()
        for (tx in txs) {
            val newTotal = currentBalance

            updated += tx.copy(totalAmount = newTotal.toPlainString())

            val amt = tx.amount.toBigDecimalOrNull() ?: BigDecimal.ZERO
            currentBalance = if (tx.isIncome) {
                currentBalance - amt
            } else {
                currentBalance + amt
            }
        }

        if (updated.isNotEmpty()) {
            transactionDao.upsert(updated)
        }
    }
}
