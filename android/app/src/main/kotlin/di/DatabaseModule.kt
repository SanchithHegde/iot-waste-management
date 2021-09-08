package me.sanchithhegde.wastecollection.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import me.sanchithhegde.wastecollection.data.AppDatabase
import me.sanchithhegde.wastecollection.data.MessageDao

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
  @Singleton
  @Provides
  fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
    return AppDatabase.getInstance(context)
  }

  @Provides
  fun provideMessageDao(appDatabase: AppDatabase): MessageDao {
    return appDatabase.messageDao()
  }
}
