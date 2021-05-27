package me.sanchithhegde.wastecollection

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.sanchithhegde.wastecollection.data.Message
import me.sanchithhegde.wastecollection.data.MessageRepository
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject internal constructor(messageRepository: MessageRepository) :
    ViewModel() {
    val messages: LiveData<List<Message>> = messageRepository.getAllMessages()
}
