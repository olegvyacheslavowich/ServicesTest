package ru.elipson.servicestest

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

class MyJobIntentService : JobIntentService() {

    override fun onCreate() {
        super.onCreate()
    }


    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy intent service")
    }

    override fun onHandleWork(intent: Intent) {
        log("onHandleIntent intent service")
        val pageValue = intent?.getIntExtra(KEY_PAGE_NUMBER, 0) ?: 0
        for (i in 0 until 100) {
            Thread.sleep(1000)
            log("page $pageValue; time $i")
        }
    }

    private fun log(s: String) {
        Log.d("SERVICE TAG", s)
    }

    companion object {

        const val KEY_PAGE_NUMBER = "page_number"
        const val JOB_ID = 1
        const val EXTRA_START = "extra_start"

        fun enqueue(context: Context, pageNumber: Int) {
            enqueueWork(
                context,
                MyJobIntentService::class.java,
                JOB_ID,
                newIntent(context, pageNumber)
            )
        }

        private fun newIntent(context: Context, startFrom: Int): Intent =
            Intent(context, MyJobIntentService::class.java)
                .putExtra(
                    EXTRA_START,
                    startFrom
                )
    }
}