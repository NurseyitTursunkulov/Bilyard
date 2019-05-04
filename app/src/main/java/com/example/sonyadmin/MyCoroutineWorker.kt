package com.example.sonyadmin

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class MyCoroutineWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override val coroutineContext = Dispatchers.IO
    override suspend fun doWork(): Result = coroutineScope {
        val jobs = (0 until 100).map {
            async {
//                downloadSynchronously("https://www.google.com")
            }
        }

        // awaitAll will throw an exception if a download fails, which MyCoroutineWorker will treat as a failure
        jobs.awaitAll()
        Result.success()
    }
}