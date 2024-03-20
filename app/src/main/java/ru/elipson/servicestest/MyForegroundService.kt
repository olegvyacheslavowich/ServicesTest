package ru.elipson.servicestest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyForegroundService : Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(1, createNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand foreground service")
        val startValue = intent?.getIntExtra(EXTRA_START, 0) ?: 0
        coroutineScope.launch {
            for (i in startValue until 100) {
                delay(500)
                log(i.toString())
            }
           // stopSelf()
        }

        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy foreground service")
        coroutineScope.cancel()
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
        fun newIntent(context: Context, startFrom: Int): Intent =
            Intent(context, MyForegroundService::class.java).putExtra(EXTRA_START, startFrom)
    }
}