package ru.elipson.servicestest

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class MyWorker(context: Context, private val workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        log("doWork started")
        val pageValue = workerParams.inputData.getInt(KEY_PAGE_NUMBER, 0)
        for (i in 0 until 100) {
            Thread.sleep(1000)
            log("page $pageValue; time $i")
        }
        return Result.success()
    }

    private fun log(s: String) {
        Log.d("SERVICE_TAG", s)
    }

    companion object {
        const val KEY_PAGE_NUMBER = "page_number"
        const val NAME = "my_worker"

        fun makeRequest(pageNumber: Int): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<MyWorker>()
                .setInputData(workDataOf(KEY_PAGE_NUMBER to pageNumber))
                .setConstraints(makeConstraints())
                .build()
        }

        private fun makeConstraints(): Constraints {
            return Constraints.Builder()
                .setRequiresCharging(true)
                .build()
        }
    }
}