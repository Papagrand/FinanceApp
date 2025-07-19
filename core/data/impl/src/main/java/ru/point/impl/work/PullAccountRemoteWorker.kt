package ru.point.impl.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.time.Instant
import kotlinx.coroutines.flow.first
import ru.point.impl.model.accountToAccountDto
import ru.point.impl.model.toAccountEntity
import ru.point.impl.service.AccountService
import ru.point.local.dao.AccountDao
import ru.point.utils.network.NetworkTracker

class PullAccountRemoteWorker(
    ctx: Context,
    params: WorkerParameters,
    private val dao: AccountDao,
    private val api: AccountService,
    private val network: NetworkTracker,
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        if (!network.online.first()) return Result.retry()

        val remote = api.getAccounts().first()
        val local = dao.observe().first()

        if (local == null ||
            local.updatedAtMillis < Instant.parse(remote.updatedAt).toEpochMilli()
        ) {
            dao.upsert(remote.accountToAccountDto().toAccountEntity(isSynced = true))
        }
        return Result.success()
    }
}