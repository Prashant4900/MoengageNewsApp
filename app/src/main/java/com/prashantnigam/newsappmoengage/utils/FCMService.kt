package com.prashantnigam.newsappmoengage.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.prashantnigam.newsappmoengage.MainActivity
import com.prashantnigam.newsappmoengage.R
import com.prashantnigam.newsappmoengage.PushNotificationManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Service class for handling Firebase Cloud Messaging (FCM) messages.
 */
class FCMService : FirebaseMessagingService() {

    /**
     * Called when a new FCM token is generated or refreshed.
     *
     * @param token The new FCM token.
     */
    @OptIn(DelicateCoroutinesApi::class)
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("Push notification", "Token Refreshed $token")

        // Register the new token on the server
        GlobalScope.launch {
            PushNotificationManager.registerTokenOnServer(token)
        }
    }

    /**
     * Called when a new FCM message is received.
     *
     * @param message The received FCM message.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("Push notification", "Message received")

        // Check if the message contains data and notification payload
        if (message.data.isEmpty() || message.notification == null) {
            return
        }

        Log.d("Push notification", "Message data payload: ${message.data}")

        // Extract message data
        val messageData = message.data

        // Check if the 'count' parameter is present in the message data
        var count: Int? = null
        if ("count" in messageData)
            count = messageData["count"]?.toInt()

        // If 'count' parameter is not present, return
        if (count == null) {
            return
        }

        // Send notification
        sendNotification(message.notification!!, count)
    }

    /**
     * Sends a notification based on the received FCM notification and count.
     *
     * @param notification The FCM notification.
     * @param count The count value to display in the notification.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(notification: RemoteMessage.Notification, count: Int) {
        // Create intent to open MainActivity when notification is clicked
        val intent = Intent(
            this,
            MainActivity::class.java
        )
        intent.putExtra("count", count.toString())

        // Create a pending intent
        val requestCode = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Set notification channel and builder
        val channelId = "FCMDemoChannel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notification.title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(notification.body)
            )
            .setShowWhen(true)
            .setWhen(System.currentTimeMillis())
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        // Get notification manager
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel
        val channel = NotificationChannel(
            channelId,
            notification.title,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.setShowBadge(true)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        manager.createNotificationChannel(channel)

        // Notify
        val notificationId = System.currentTimeMillis().toInt()
        manager.notify(notificationId, builder.build())
    }
}
