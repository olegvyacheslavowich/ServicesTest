package ru.elipson.servicestest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import android.content.ComponentName
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import ru.elipson.servicestest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw RuntimeException("binding is null")

    private var page: Int = 0
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

        binding.jobSchedulerService.setOnClickListener {
            val componentName = ComponentName(this, MyJobService::class.java) //указываем сервис
            val jobInfo =
                JobInfo.Builder(MyJobService.ID, componentName)  //устанавливаем ограничения
                    .setRequiresCharging(true) //только если включена зарядка
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED) //только если подключен к вай фай
                    .build()
            val jobScheduler =
                getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler //запускаем на выполнение
            //   jobScheduler.schedule(jobInfo) //не складывает сервисы в очередь. Каждый следующий сервис отменяет предыдущий

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = MyJobService.newIntent(page++)
                jobScheduler.enqueue(
                    jobInfo,
                    JobWorkItem(intent)
                )
            } //складывает сервисы в очередь. Сервисы выполняются по очереди
            else {
                startService(MyIntentService.newIntent(this, page++))
            }
        }

        binding.jobIntentService.setOnClickListener {
            MyJobIntentService.enqueue(this, page++)
        }

        binding.workerService.setOnClickListener {
            val workManager = WorkManager.getInstance(applicationContext)
            workManager.enqueueUniqueWork(
                MyWorker.NAME,
                ExistingWorkPolicy.REPLACE,
                MyWorker.makeRequest(page++)
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