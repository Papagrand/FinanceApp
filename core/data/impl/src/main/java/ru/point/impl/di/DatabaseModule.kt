package ru.point.impl.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.point.local.AppDatabase
import ru.point.local.dao.AccountDao
import ru.point.local.dao.CategoryDao
import ru.point.local.dao.TransactionDao

@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "finance_app.db"
        ).build()

    @Provides
    @Singleton
    fun provideTransactionDao(db: AppDatabase): TransactionDao =
        db.transactionDao()

    @Provides
    @Singleton
    fun provideCategoryDao(db: AppDatabase): CategoryDao =
        db.categoryDao()

    @Provides
    @Singleton
    fun provideAccountDao(db: AppDatabase): AccountDao =
        db.accountDao()


    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}