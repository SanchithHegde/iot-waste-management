package me.sanchithhegde.wastecollection

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.sanchithhegde.wastecollection.data.AppDatabase
import me.sanchithhegde.wastecollection.data.MessageDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun providesMessageDao(appDatabase: AppDatabase): MessageDao {
        return appDatabase.messageDao()
    }
}