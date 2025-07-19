package ru.point.impl.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.time.Instant
import kotlinx.coroutines.flow.first
import ru.point.impl.model.AccountUpdateRequest
import ru.point.impl.service.AccountService
import ru.point.local.dao.AccountDao
import ru.point.utils.network.NetworkTracker

class PushAccountPendingWorker(
    ctx: Context,
    params: WorkerParameters,
    private val dao: AccountDao,
    private val api: AccountService,
    private val network: NetworkTracker,
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        if (!network.online.first()) return Result.retry()

        dao.pending().forEach { entity ->
            val res = runCatching {
                api.updateAccount(
                    entity.id,
                    AccountUpdateRequest(entity.name, entity.balance, entity.currency)
                )
            }
            if (res.isFailure) return Result.retry()

            val dto = res.getOrThrow()
            dao.markSynced(
                id = dto.id,
                updatedAt = dto.updatedAt,
                updatedAtMillis = Instant.parse(dto.updatedAt).toEpochMilli()
            )
        }
        return Result.success()
    }
}