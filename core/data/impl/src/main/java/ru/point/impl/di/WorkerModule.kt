package ru.point.impl.di

import androidx.work.WorkerFactory
import dagger.Binds
import dagger.Module
import javax.inject.Singleton
import ru.point.impl.network.NetworkTrackerImpl
import ru.point.impl.work.DaggerWorkerFactory
import ru.point.utils.network.NetworkTracker

@Module
abstract class WorkerModule {
    @Binds
    @Singleton
    abstract fun bindWorkerFactory(
        factory: DaggerWorkerFactory
    ): WorkerFactory

    @Binds
    @Singleton
    abstract fun bindNetworkTracker(impl: NetworkTrackerImpl): NetworkTracker
}