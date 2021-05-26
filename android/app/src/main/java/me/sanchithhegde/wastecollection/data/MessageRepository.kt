package me.sanchithhegde.wastecollection.data

import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import me.sanchithhegde.wastecollection.WasteCollectionApplication
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(private val messageDao: MessageDao) {
    fun getAllMessages() =
        Transformations.map(
            messageDao.getAllMessagesDistinctUntilChanged().asLiveData()
        ) { messageList ->
            messageList.map { messageEntity ->
                messageEntity.toMessage(WasteCollectionApplication.instance.applicationContext)
            }
        }
}