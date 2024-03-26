package com.prashantnigam.newsappmoengage

import android.util.Log
import kotlinx.coroutines.delay

object PushNotificationManager {
    private var countReceived: Int = 0

    fun setDataReceived(count: Int) {
        countReceived = count
    }

    suspend fun registerTokenOnServer(token: String) {
        Log.d("registerTokenOnServer", token)
        delay(2000)
    }
}