package me.sanchithhegde.wastecollection.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import me.sanchithhegde.wastecollection.database.entities.Message

@Dao
interface MessageDao {
    @Insert
    fun insert(message: Message)

    @Query("SELECT * FROM messages")
    fun getAllMessages(): List<Message>

}