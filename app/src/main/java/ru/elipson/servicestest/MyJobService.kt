package ru.elipson.servicestest

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PersistableBundle
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyJobService : JobService() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartJob(params: JobParameters?): Boolean {
        log("onStartJob service")
        coroutineScope.launch {
            var workItem = params?.dequeueWork() // берем ворк айтем из очереди
            while (workItem != null) {
                //получаем параметры
                val pageNumber = workItem.intent.getIntExtra(KEY_PAGE_NUMBER, 0)
                //выполяем логику
                for (i in 0 until 5) {
                    delay(1000)
                    log("page $pageNumber; time $i")
                }
                //завершаем работу текущего ворк айтема
                params?.completeWork(workItem)
                //удаляем ворк айтем из очереди
                workItem = params?.dequeueWork()
            }
            //после выполнения всех ворк айтемов завершаем работу сервиса
            jobFinished(params, false)
        }
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        log("onStopJob service")
        return true
    }

    override fun onCreate() {
        super.onCreate()
        log("onCreate service")

    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy service")
        coroutineScope.cancel()
    }

    private fun log(s: String) {
        Log.d("SERVICE TAG", s)
    }

    companion object {
        const val EXTRA_START = "extra_start"
        const val KEY_PAGE_NUMBER = "page_number"
        const val ID = 1

        fun newBundle(page: Int): PersistableBundle {
            return PersistableBundle().apply {
                putInt(KEY_PAGE_NUMBER, page)
            }
        }

        fun newIntent(page: Int): Intent =
            Intent().apply {
                putExtra(
                    KEY_PAGE_NUMBER, page
                )
            }
    }
}