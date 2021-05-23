package me.sanchithhegde.wastecollection.notifications

import android.content.Context
import androidx.room.Room
import com.onesignal.OSNotification
import com.onesignal.OSNotificationReceivedEvent
import com.onesignal.OneSignal
import me.sanchithhegde.wastecollection.database.AppDatabase
import me.sanchithhegde.wastecollection.database.entities.Message
import java.time.Instant

class NotificationServiceExtension : OneSignal.OSRemoteNotificationReceivedHandler {
    override fun remoteNotificationReceived(
        context: Context?,
        notificationReceivedEvent: OSNotificationReceivedEvent?
    ) {
        notificationReceivedEvent?.let {
            val notification: OSNotification =
                notificationReceivedEvent.notification

            context?.let {
                val db =
                    Room.databaseBuilder(context, AppDatabase::class.java, "messages.db").build()
                val messageDao = db.messageDao()

                messageDao.insert(
                    Message(
                        Instant.now().toEpochMilli(),
                        notification.title,
                        notification.body
                    )
                )
            }

            notificationReceivedEvent.complete(notification)
        }
    }
}