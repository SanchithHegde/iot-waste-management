package me.sanchithhegde.wastecollection.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface MessageDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract fun insertMessage(message: MessageEntity)

  @Query("SELECT * FROM messages") abstract fun getAllMessages(): Flow<List<MessageEntity>>

  fun getAllMessagesDistinctUntilChanged() = getAllMessages().distinctUntilChanged()
}
