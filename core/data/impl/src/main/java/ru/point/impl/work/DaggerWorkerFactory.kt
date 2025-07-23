package ru.point.impl.work

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import ru.point.api.flow.AccountPreferencesRepo
import ru.point.impl.service.AccountService
import ru.point.impl.service.TransactionService
import ru.point.local.dao.AccountDao
import ru.point.local.dao.TransactionDao
import ru.point.utils.network.NetworkTracker

class DaggerWorkerFactory @Inject constructor(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val transactionApi: TransactionService,
    private val accountApi: AccountService,
    private val prefs: AccountPreferencesRepo,
    private val networkTracker: NetworkTracker
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? = when (workerClassName) {
        PushPendingWorker::class.java.name ->
            PushPendingWorker(appContext, workerParameters, transactionDao, transactionApi, prefs, networkTracker)

        PullRemoteWorker::class.java.name ->
            PullRemoteWorker(appContext, workerParameters, transactionDao, transactionApi, prefs, networkTracker)

        PushAccountPendingWorker::class.java.name ->
            PushAccountPendingWorker(appContext, workerParameters, accountDao, accountApi, networkTracker)

        PullAccountRemoteWorker::class.java.name ->
            PullAccountRemoteWorker(appContext, workerParameters, accountDao, accountApi, networkTracker, prefs)

        else -> null
    }
}
