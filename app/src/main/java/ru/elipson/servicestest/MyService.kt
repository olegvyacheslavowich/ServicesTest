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
        coroutineScope.launch {
            for (i in 0 until 100) {
                delay(1000)
                log(i.toString())
            }
        }

        return super.onStartCommand(intent, flags, startId)
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
        fun newIntent(context: Context): Intent =
            Intent(context, MyService::class.java)
    }
}