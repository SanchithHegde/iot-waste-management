package me.sanchithhegde.wastecollection.data

import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(private val messageDao: MessageDao) {
    fun getAllMessages() = messageDao.getAllMessagesDistinctUntilChanged()

    fun insertMessage(title: String, body: String) {
        messageDao.insertMessage(MessageEntity(Instant.now().toEpochMilli(), title, body))
    }
}
