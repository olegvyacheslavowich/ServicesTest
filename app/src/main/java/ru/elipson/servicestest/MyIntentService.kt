package ru.elipson.servicestest

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

class MyIntentService : IntentService(NAME) {

    override fun onCreate() {
        super.onCreate()
        startForeground(1, createNotification())
        setIntentRedelivery(true)
    }


    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy intent service")
    }

    override fun onHandleIntent(intent: Intent?) {
        log("onHandleIntent intent service")
        val startValue = intent?.getIntExtra(EXTRA_START, 0) ?: 0
        for (i in startValue until 100) {
            Thread.sleep(1000)
            log(i.toString())
        }

    }

    private fun log(s: String) {
        Log.d("SERVICE TAG", s)
    }

    private fun createNotification(): Notification {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chanel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(chanel)
        }
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Foreground service test")
            .setContentText("service is running...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }

    companion object {

        const val EXTRA_START = "extra_start"
        const val NOTIFICATION_CHANNEL_ID = "foreground_service_id"
        const val NOTIFICATION_CHANNEL_NAME = "foreground_service_name"
        const val NAME = "my_intent_service"
        fun newIntent(context: Context, startFrom: Int): Intent =
            Intent(context, MyIntentService::class.java).putExtra(EXTRA_START, startFrom)
    }
}