package ru.point.impl.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.time.Instant
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import ru.point.impl.flow.safeApiFlow
import ru.point.impl.model.AccountUpdateRequest
import ru.point.impl.service.AccountService
import ru.point.local.dao.AccountDao
import ru.point.utils.network.NetworkTracker
import androidx.work.ListenableWorker.Result as WorkResult
import ru.point.utils.common.Result as DomainResult

class PushAccountPendingWorker(
    ctx: Context,
    params: WorkerParameters,
    private val dao: AccountDao,
    private val api: AccountService,
    private val networkTracker: NetworkTracker,
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): WorkResult {
        val onlineNow = networkTracker.online.first()
        if (!onlineNow) return WorkResult.retry()

        val pending = dao.pending()
        if (pending.isEmpty()) return WorkResult.success()

        for (entity in pending) {
            val syncFlow = safeApiFlow {
                api.updateAccount(
                    entity.id,
                    AccountUpdateRequest(
                        name = entity.name,
                        balance = entity.balance,
                        currency = entity.currency
                    )
                )
            }
            val final = syncFlow
                .filter { it !is DomainResult.Loading }
                .first()

            when (final) {
                is DomainResult.Error   -> return WorkResult.retry()
                is DomainResult.Success -> {
                    val dto = final.data
                    dao.markSynced(
                        id              = dto.id,
                        updatedAt       = dto.updatedAt,
                        updatedAtMillis = Instant.parse(dto.updatedAt).toEpochMilli()
                    )
                }
                else -> {
                }
            }
        }

        return WorkResult.success()
    }
}