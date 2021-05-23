package me.sanchithhegde.wastecollection.notifications

import android.content.Context
import android.util.Log
import com.onesignal.OSNotification
import com.onesignal.OSNotificationReceivedEvent
import com.onesignal.OneSignal

class NotificationServiceExtension : OneSignal.OSRemoteNotificationReceivedHandler {
    override fun remoteNotificationReceived(
        context: Context?,
        notificationReceivedEvent: OSNotificationReceivedEvent?
    ) {
        notificationReceivedEvent?.let {
            val notification: OSNotification =
                notificationReceivedEvent.notification

            Log.i(
                "wc_notification",
                "Notification received: (${notification.title}, ${notification.body})"
            )

            notificationReceivedEvent.complete(notification)
        }
    }
}