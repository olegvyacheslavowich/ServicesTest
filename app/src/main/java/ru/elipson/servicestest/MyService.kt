package ru.elipson.servicestest

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyService : Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        log("onCreate service")

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand service")
        val startValue = intent?.getIntExtra(EXTRA_START, 0) ?: 0
        coroutineScope.launch {
            for (i in startValue until 20) {
                delay(1000)
                log(i.toString())
            }
            stopSelf()
        }

        return START_REDELIVER_INTENT
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
        fun newIntent(context: Context, startFrom: Int): Intent =
            Intent(context, MyService::class.java).putExtra(EXTRA_START, startFrom)
    }
}