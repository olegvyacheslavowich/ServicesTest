package ru.elipson.servicestest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import ru.elipson.servicestest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw RuntimeException("binding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.simpleService.setOnClickListener {
            stopService(MyForegroundService.newIntent(context = applicationContext, 15))
            startService(MyService.newIntent(context = applicationContext, 15))
        }

        binding.simpleForegroundService.setOnClickListener {
            //showNotification()
            //startService(MyForegroundService.newIntent(context = applicationContext, 0))
            ContextCompat.startForegroundService(
                this,
                MyForegroundService.newIntent(context = applicationContext, 0)
            )
        }

        binding.simpleIntentService.setOnClickListener {
            ContextCompat.startForegroundService(
                this,
                MyIntentService.newIntent(this, 0)
            )
        }

    }

    private fun showNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nChannel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(nChannel)
        }
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentText("Service test") //заголовок
            .setContentText("Running...") // текст уведомления
            .setSmallIcon(R.drawable.ic_launcher_foreground) //установка иконки
            .build()

        notificationManager.notify(1, notification)
    }

    companion object {
        const val CHANNEL_ID = "test_channel"
        const val CHANNEL_NAME = "test channel"
    }
}