package ru.point.impl.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.point.api.flow.AccountPreferencesRepo
import ru.point.impl.flow.safeApiFlow
import ru.point.impl.model.toCreateRequest
import ru.point.impl.service.TransactionService
import ru.point.local.dao.TransactionDao
import ru.point.utils.network.NetworkTracker
import androidx.work.ListenableWorker.Result as WorkResult
import ru.point.utils.common.Result as DomainResult

class PushPendingWorker(
    ctx: Context,
    params: WorkerParameters,
    private val dao: TransactionDao,
    private val api: TransactionService,
    private val prefs: AccountPreferencesRepo,
    private val networkTracker: NetworkTracker,
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): WorkResult {
        val onlineNow = networkTracker.online.first()

        if (!onlineNow) {
            return WorkResult.retry()
        }

        val pending = dao.pendingAll()
        if (pending.isEmpty()) {
            return Result.success()
        }

        for (entity in pending) {
            if (entity.isDeleted) {
                if (entity.remoteId > 0) {
                    safeApiFlow { api.deleteTransaction(entity.remoteId) }
                        .filter { it !is DomainResult.Loading }
                        .first().let { res ->
                            if (res is DomainResult.Error) return Result.retry()
                        }
                }
                dao.hardDeleteLocal(entity.localUid)
            }else{
                val request = entity.toCreateRequest()
                val idResultFlow: Flow<DomainResult<Int>> = if (entity.remoteId < 0) {
                    safeApiFlow { api.createNewTransaction(request) }
                        .map { res ->
                            when (res) {
                                is DomainResult.Loading -> DomainResult.Loading
                                is DomainResult.Error -> DomainResult.Error(res.cause)
                                is DomainResult.Success ->
                                    DomainResult.Success(res.data.id)
                            }
                        }
                } else {
                    safeApiFlow { api.updateTransaction(entity.remoteId, request) }
                        .map { res ->
                            when (res) {
                                is DomainResult.Loading -> DomainResult.Loading
                                is DomainResult.Error -> DomainResult.Error(res.cause)
                                is DomainResult.Success ->
                                    DomainResult.Success(res.data.id)
                            }
                        }
                }

                val final = idResultFlow.first{it !is DomainResult.Loading }

                when (final) {
                    is DomainResult.Loading -> {}
                    is DomainResult.Error -> return WorkResult.retry()
                    is DomainResult.Success -> {
                        dao.markSynced(entity.localUid, final.data)
                    }
                }
            }
        }

        prefs.updateLastSync(Instant.now().toString())

        return Result.success()
    }
}