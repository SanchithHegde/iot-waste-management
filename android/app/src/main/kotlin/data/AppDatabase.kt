package me.sanchithhegde.wastecollection.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.sanchithhegde.wastecollection.utilities.DATABASE_NAME

@Database(entities = [MessageEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract fun messageDao(): MessageDao

  companion object {
    // For Singleton instantiation
    @Volatile private var instance: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
      return instance
        ?: synchronized(this) {
          instance ?: buildDatabase(context).also { appDatabase -> instance = appDatabase }
        }
    }

    // Create the database
    private fun buildDatabase(context: Context): AppDatabase {
      return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
    }
  }
}
