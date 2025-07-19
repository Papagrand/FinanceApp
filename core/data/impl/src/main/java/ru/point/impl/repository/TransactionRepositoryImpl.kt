package ru.point.impl.repository

import java.time.Instant
import kotlinx.coroutines.flow.Flow
import ru.point.api.model.CreateTransactionResponseDto
import ru.point.api.model.TransactionDto
import ru.point.api.repository.TransactionRepository
import ru.point.impl.flow.safeApiFlow
import ru.point.impl.model.CreateTransactionRequest
import ru.point.impl.model.Transaction
import ru.point.impl.model.toDto
import ru.point.impl.model.entityToDto
import ru.point.impl.service.TransactionService
import ru.point.utils.common.Result
import ru.point.utils.common.Result.Error
import ru.point.utils.common.Result.Loading
import ru.point.utils.common.Result.Success
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.point.api.flow.AccountPreferencesRepo
import ru.point.impl.model.fromCreateTransactionToEntity
import ru.point.impl.model.toCreateRequest
import ru.point.impl.model.toCreateResponseDto
import ru.point.impl.model.toCreateTransactionDto
import ru.point.impl.model.toEntity
import ru.point.local.dao.TransactionDao
import ru.point.local.entities.TransactionEntity
import ru.point.utils.model.toAppError
import ru.point.utils.network.NetworkTracker

/**
 * TransactionRepositoryImpl
 *
 * Ответственность:
 * - запрашивать транзакции из API через Retrofit;
 * - преобразовывать DTO в доменную модель Transaction;
 * - оборачивать результаты в Flow<Result<List<Transaction>>>.
 *
 */

class TransactionRepositoryImpl @Inject constructor(
    private val api: TransactionService,
    private val dao: TransactionDao,
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
                                accountName = accountName,
                                currency = currency,
                                categoryName = categoryName,
                                emoji = emoji,
                                isIncome = isIncome
                            )
                            dao.upsert(entity)
                            prefs.updateLastSync(Instant.now().toString())
                            send(Success(dto.toCreateTransactionDto()))
                        }
                    }
                }
        } else {
            val fakeId = -(System.currentTimeMillis() % 100_000_000).toInt()
            val draft = TransactionEntity(
                localId = 0L,
                remoteId = fakeId,
                accountId = accountId,
                accountName = accountName,
                amount = amount,
                currency = currency,
                categoryId = categoryId,
                categoryName = categoryName,
                emoji = emoji,
                isIncome = isIncome,
                dateTime = transactionDate,
                comment = comment,
                totalAmount = "",
                updatedAt = now,
                isSynced = false,
                isDeleted = false
            )
            dao.upsert(draft)
            send(Success(draft.toCreateResponseDto()))
        }
    }.flowOn(dispatcher)

    override fun deleteTransaction(transactionId: Int): Flow<Result<Unit>> = channelFlow {
        send(Loading)

        val entity = dao.getByRemoteId(transactionId)
        if (entity != null) {
            dao.softDelete(transactionId)
        } else {
            dao.hardDeleteLocal(localUid = transactionId.toString())
        }
        send(Success(Unit))

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
    }.flowOn(dispatcher)

    override fun observePeriod(
        accountId: Int,
        startDateIso: String,
        endDateIso: String,
    ): Flow<Result<List<TransactionDto>>> = channelFlow {
        send(Loading)
        launch(dispatcher) {
            if (!dao.hasData(accountId, startDateIso, endDateIso)) {
                fetchPeriodAndCache(accountId, startDateIso, endDateIso)
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
    ): Flow<Result<TransactionDto>> = channelFlow {
        send(Loading)

        val local = dao.requireByRemoteId(transactionId)
        val nowMillis = System.currentTimeMillis()
        val updated = local.copy(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            dateTime = transactionDate,
            comment = comment,
            updatedAt = nowMillis,
            isSynced = false
        )
        dao.upsert(updated)
        send(Success(updated.entityToDto()))

        if (networkTracker.online.first()) {
            safeApiFlow {
                api.updateTransaction(transactionId, updated.toCreateRequest())
            }.collect { apiResult ->
                when (apiResult) {
                    is Loading -> {
                    }

                    is Error -> {
                        send(Error(apiResult.cause))
                    }

                    is Success -> {
                        dao.markSynced(updated.localUid, transactionId)
                        send(Success(apiResult.data.toDto()))
                    }
                }
            }
        }
    }.flowOn(dispatcher)

    private suspend fun fetchPeriodAndCache(
        accountId: Int,
        fromIso: String,
        toIso: String
    ) {
        safeApiFlow {
            api.getByAccountForPeriod(accountId, fromIso, toIso)
                .map { it.toDto() }
        }
            .filterIsInstance<Success<List<TransactionDto>>>()
            .firstOrNull()?.data?.map { it.toEntity(isSynced = true) }
            ?.let {
                dao.upsert(it)
                prefs.updateLastSync(Instant.now().toString())
            }

    }

    private suspend fun syncIfRemoteNewer(
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
            ?.let {
                dao.upsert(it)
                prefs.updateLastSync(Instant.now().toString())
            }
    }

    private fun String.parseIsoMillis(): Long =
        Instant.parse(this).toEpochMilli()
}
