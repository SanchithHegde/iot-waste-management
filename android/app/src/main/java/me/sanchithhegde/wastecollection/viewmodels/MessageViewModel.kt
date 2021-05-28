package me.sanchithhegde.wastecollection.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import me.sanchithhegde.wastecollection.MainApplication
import me.sanchithhegde.wastecollection.data.Message
import me.sanchithhegde.wastecollection.data.MessageRepository
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject internal constructor(messageRepository: MessageRepository) :
    ViewModel() {
    val messages: LiveData<List<Message>> = Transformations.map(
        messageRepository.getAllMessages().asLiveData()
    ) { messageList ->
        messageList.map { messageEntity ->
            messageEntity.toMessage(MainApplication.instance.applicationContext)
        }
    }
}
