package ru.point.impl.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import ru.point.api.flow.AccountPreferencesRepo
import ru.point.impl.model.toDto
import ru.point.impl.model.toEntity
import ru.point.impl.service.TransactionService
import ru.point.local.dao.TransactionDao
import ru.point.utils.network.NetworkTracker
import androidx.work.ListenableWorker.Result as WorkResult

class PullRemoteWorker(
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

        val today = LocalDate.now()
        val start = today.minusDays(30).format(DateTimeFormatter.ISO_DATE)
        val end = today.format(DateTimeFormatter.ISO_DATE)

        val accountId = prefs.accountIdFlow.firstOrNull()

        if (accountId != null) {
            val remoteList = api.getByAccountForPeriod(accountId, start, end)
            val localList = dao.rawPeriod(accountId, start, end)

            remoteList.forEach { r ->
                val l = localList.find { it.remoteId == r.id }
                if (l == null || l.updatedAt < Instant.parse(r.updatedAt).toEpochMilli()) {
                    val existing = dao.getByRemoteId(r.id)
                    val entity = r.toDto()
                        .toEntity(isSynced = true)
                        .copy(
                            localId = existing?.localId ?: 0L,
                            remoteId = r.id,
                            updatedAt = Instant.parse(r.updatedAt).toEpochMilli()
                        )
                    dao.upsert(entity)
                }
            }
            return WorkResult.success()
        } else {
            return WorkResult.failure()
        }
    }
}