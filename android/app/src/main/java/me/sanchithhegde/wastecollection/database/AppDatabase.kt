package me.sanchithhegde.wastecollection.database

import androidx.room.Database
import androidx.room.RoomDatabase
import me.sanchithhegde.wastecollection.database.daos.MessageDao
import me.sanchithhegde.wastecollection.database.entities.Message

@Database(entities = [Message::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}
