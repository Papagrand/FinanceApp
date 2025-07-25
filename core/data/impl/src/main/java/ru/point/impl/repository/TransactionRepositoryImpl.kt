package ru.point.impl.repository

import java.math.BigDecimal
import java.time.Instant
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.point.api.flow.AccountPreferencesRepo
import ru.point.api.model.CreateTransactionResponseDto
import ru.point.api.model.TransactionDto
import ru.point.api.repository.TransactionRepository
import ru.point.impl.flow.safeApiFlow
import ru.point.impl.model.CreateTransactionRequest
import ru.point.impl.model.Transaction
import ru.point.impl.model.entityToDto
import ru.point.impl.model.fromCreateTransactionToEntity
import ru.point.impl.model.toCreateRequest
import ru.point.impl.model.toCreateResponseDto
import ru.point.impl.model.toCreateTransactionDto
import ru.point.impl.model.toDto
import ru.point.impl.model.toEntity
import ru.point.impl.service.TransactionService
import ru.point.local.dao.AccountDao
import ru.point.local.dao.CategoryDao
import ru.point.local.dao.TransactionDao
import ru.point.local.entities.TransactionEntity
import ru.point.utils.common.Result
import ru.point.utils.common.Result.Error
import ru.point.utils.common.Result.Loading
import ru.point.utils.common.Result.Success
import ru.point.utils.model.toAppError
import ru.point.utils.network.NetworkTracker


class TransactionRepositoryImpl @Inject constructor(
    private val api: TransactionService,
    private val dao: TransactionDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    private val networkTracker: NetworkTracker,
    private val prefs: AccountPreferencesRepo,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : TransactionRepository {

    override fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?,
        accountName: String,
        categoryName: String,
        emoji: String
    ): Flow<Result<CreateTransactionResponseDto>> = channelFlow {
        val isIncome = categoryId <= 6
        val currency = prefs.currencyFlow.firstOrNull() ?: "RUB"
        val now = System.currentTimeMillis()

        val request = CreateTransactionRequest(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = transactionDate,
            comment = comment
        )
        val delta: BigDecimal = amount.toBigDecimal()

        val accountEntity = accountDao.observe().firstOrNull()
            ?: throw IllegalStateException("Account with id=$accountId not found")
        val currentBal = accountEntity.balance.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val newBalance = if (isIncome) currentBal + delta else currentBal - delta


        val updatedAccount = accountEntity.copy(
            balance = newBalance.toPlainString(),
            updatedAtMillis = now,
            updatedAt = Instant.ofEpochMilli(now).toString(),
            isSynced = false
        )

        if (networkTracker.online.first()) {
            send(Loading)
            safeApiFlow { api.createNewTransaction(request) }
                .collect { apiResult ->
                    when (apiResult) {
                        is Loading -> {
                        }

                        is Error -> {
                            send(apiResult)
                        }

                        is Success -> {
                            val dto = apiResult.data
                            val entity = dto.fromCreateTransactionToEntity(
                                accountName = accountEntity.name,
                                currency = currency,
                                categoryName = categoryName,
                                emoji = emoji,
                                isIncome = isIncome,
                                totalAmount = newBalance.toString()
                            )
                            dao.upsert(entity)
                            accountDao.upsert(updatedAccount)
                            prefs.updateLastSync(Instant.now().toString())
                            recalculateTransactionTotals(accountEntity.userId, newBalance.toPlainString())
                            send(Success(dto.toCreateTransactionDto()))
                        }
                    }
                }
        } else {
            accountDao.upsert(updatedAccount)

            val fakeId = -(System.currentTimeMillis() % 100_000_000).toInt()
            val draft = TransactionEntity(
                localId = 0L,
                remoteId = fakeId,
                accountId = accountId,
                accountName = accountEntity.name,
                amount = amount,
                currency = currency,
                categoryId = categoryId,
                categoryName = categoryName,
                emoji = emoji,
                isIncome = isIncome,
                dateTime = transactionDate,
                comment = comment,
                totalAmount = newBalance.toPlainString(),
                updatedAt = now,
                isSynced = false,
                isDeleted = false
            )
            dao.upsert(draft)

            recalculateTransactionTotals(accountEntity.userId, newBalance.toPlainString())

            send(Success(draft.toCreateResponseDto()))
        }
    }.flowOn(dispatcher)

    override fun deleteTransaction(transactionId: Int, isIncome: Boolean): Flow<Result<Unit>> = channelFlow {
        send(Loading)

        val entity = dao.getByRemoteId(transactionId)

        val delta: BigDecimal = entity?.amount?.toBigDecimal() ?: BigDecimal.ZERO

        val accountEntity = accountDao.observe().firstOrNull()
            ?: throw IllegalStateException("")
        val currentBal = accountEntity.balance.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val newBalance = if (isIncome) currentBal - delta else currentBal + delta


        val updatedAccount = accountEntity.copy(
            balance = newBalance.toPlainString(),
            updatedAtMillis = System.currentTimeMillis(),
            updatedAt = Instant.ofEpochMilli(System.currentTimeMillis()).toString(),
            isSynced = false
        )

        accountDao.upsert(updatedAccount)

        if (entity != null) {
            dao.softDelete(transactionId)
        } else {
            dao.hardDeleteLocal(localUid = transactionId.toString())
        }

        if (networkTracker.online.first()) {
            dao.hardDeleteWithOnline(remoteId = transactionId)
            safeApiFlow { api.deleteTransaction(transactionId) }
                .collect { result ->
                    when (result) {
                        is Success ->
                            send(Success(Unit))

                        is Error -> send(Error(result.cause))
                        Loading -> {}
                    }
                }
        }

        recalculateTransactionTotals(accountEntity.userId, newBalance.toPlainString())

        send(Success(Unit))
    }.flowOn(dispatcher)

    override fun observePeriod(
        accountId: Int,
        startDateIso: String,
        endDateIso: String,
    ): Flow<Result<List<TransactionDto>>> = channelFlow {
        send(Loading)
        launch(dispatcher) {
            if (!dao.hasData(accountId, startDateIso, endDateIso)) {
                val hasAny = fetchPeriodAndCache(accountId, startDateIso, endDateIso)
                if (!hasAny) {
                    send(Success(emptyList()))
                    return@launch
                }
            } else {
                syncIfRemoteNewer(accountId, startDateIso, endDateIso)
            }
        }
        dao.observePeriod(accountId, startDateIso, endDateIso)
            .collect { list ->
                send(Success(list.map { it.entityToDto() }))
            }
    }
        .catch { e -> emit(Error(e.toAppError())) }
        .flowOn(dispatcher)


    override fun getTransactionById(transactionId: Int): Flow<Result<TransactionDto>> =
        channelFlow {
            send(Loading)

            dao.observeByRemoteId(transactionId)
                .collect { entity ->
                    entity?.let { send(Success(it.entityToDto())) }
                }
        }
            .catch { e -> emit(Error(e.toAppError())) }
            .flowOn(dispatcher)


    override fun updateTransaction(
        transactionId: Int,
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?,
        isIncome: Boolean
    ): Flow<Result<TransactionDto>> = channelFlow {
        send(Loading)
        try {
            val local = dao.requireByRemoteId(transactionId)
            val delta = amount.toBigDecimal() - local.amount.toBigDecimal()
            val categoryName = categoryDao.getCategoryNameById(categoryId)
            val account = accountDao.observe().first() ?: throw IllegalStateException()
            val currentBal = account.balance.toBigDecimalOrNull() ?: BigDecimal.ZERO
            val newBalance = if (isIncome) currentBal + delta else currentBal - delta

            val updatedAccount = account.copy(
                balance = newBalance.toPlainString(),
                isSynced = false,
                updatedAtMillis = System.currentTimeMillis(),
                updatedAt = Instant.now().toString()
            )
            val updatedTransaction = local.copy(
                accountId = accountId,
                categoryId = categoryId,
                categoryName = categoryName,
                amount = amount,
                dateTime = transactionDate,
                comment = comment,
                updatedAt = System.currentTimeMillis(),
                isSynced = false
            )

            dao.updateTransactionsAndRecalcTotals(
                updatedAccount, updatedTransaction, accountId, newBalance.toPlainString()
            )

            if (networkTracker.online.first()) {
                send(Loading)
                safeApiFlow { api.updateTransaction(transactionId, updatedTransaction.toCreateRequest()) }
                    .collect { result ->
                        when (result) {
                            is Loading -> {}
                            is Error -> send(Error(result.cause))
                            is Success -> {
                                dao.markSynced(updatedTransaction.localUid, transactionId)
                                send(Success(result.data.toDto()))
                            }
                        }
                    }
            } else {
                send(Success(updatedTransaction.entityToDto()))
            }

        } catch (e: Throwable) {
            send(Error(e.toAppError()))
        }
    }.flowOn(dispatcher)

    private suspend fun fetchPeriodAndCache(
        accountId: Int,
        fromIso: String,
        toIso: String
    ): Boolean {

        var hasRemoteData = false

        safeApiFlow {
            api.getByAccountForPeriod(accountId, fromIso, toIso)
                .map { it.toDto() }
        }
            .filterIsInstance<Success<List<TransactionDto>>>()
            .firstOrNull()?.data
            ?.let { list ->
                if (list.isNotEmpty()) {
                    dao.upsert(list.map { it.toEntity(isSynced = true) })
                    prefs.updateLastSync(Instant.now().toString())
                    hasRemoteData = true
                }
            }

        return hasRemoteData
    }

    suspend fun syncIfRemoteNewer(
        accountId: Int,
        fromIso: String,
        toIso: String
    ) {
        val local = dao.rawPeriod(accountId, fromIso, toIso)

        safeApiFlow { api.getByAccountForPeriod(accountId, fromIso, toIso) }
            .filterIsInstance<Success<List<Transaction>>>()
            .firstOrNull()
            ?.data
            ?.filter { remote ->
                val match = local.find { it.remoteId == remote.id }
                match == null || match.updatedAt < remote.updatedAt.parseIsoMillis()
            }
            ?.takeIf { it.isNotEmpty() }
            ?.map { it.toDto().toEntity(isSynced = true) }
            ?.let { dao.upsert(it) }
    }

    private suspend fun recalculateTransactionTotals(
        accountId: Int,
        balanceStr: String
    ) {
        val txs = dao.getAllByAccountDesc(accountId)

        val updated = txs.map { tx ->
            tx.copy(totalAmount = balanceStr)
        }

        if (updated.isNotEmpty()) {
            dao.upsert(updated)
        }
    }

    private fun String.parseIsoMillis(): Long =
        Instant.parse(this).toEpochMilli()
}
