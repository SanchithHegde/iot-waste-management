package me.sanchithhegde.wastecollection.notifications

import android.content.Context
import com.onesignal.OSNotification
import com.onesignal.OSNotificationReceivedEvent
import com.onesignal.OneSignal
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import me.sanchithhegde.wastecollection.data.MessageRepository

class NotificationServiceExtension : OneSignal.OSRemoteNotificationReceivedHandler {

  @EntryPoint
  @InstallIn(SingletonComponent::class)
  interface NotificationServiceExtensionEntryPoint {
    fun messageRepository(): MessageRepository
  }

  override fun remoteNotificationReceived(
    context: Context?,
    notificationReceivedEvent: OSNotificationReceivedEvent?
  ) {
    notificationReceivedEvent?.let {
      val notification: OSNotification = notificationReceivedEvent.notification

      val appContext = context?.applicationContext ?: throw IllegalStateException()
      val hiltEntryPoint =
        EntryPointAccessors.fromApplication(
          appContext,
          NotificationServiceExtensionEntryPoint::class.java
        )
      val messageRepository = hiltEntryPoint.messageRepository()

      messageRepository.insertMessage(notification.title, notification.body)

      notificationReceivedEvent.complete(notification)
    }
  }
}
