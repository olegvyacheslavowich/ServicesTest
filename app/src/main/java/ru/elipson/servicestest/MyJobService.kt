package ru.elipson.servicestest

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyJobService : JobService() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    override fun onStartJob(params: JobParameters?): Boolean {
        log("onStartJob service")
        coroutineScope.launch {
            for (i in 0 until 20) {
                delay(1000)
                log(i.toString())
            }
            jobFinished(params, true)
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
        const val ID = 1
    }
}