package com.example.sonyadmin

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class EveryDayUpdateCashWorkerImpl : EveryDayUpdateCashWorker {
    override fun update() {
        val uploadWorkRequest = OneTimeWorkRequestBuilder<MyCoroutineWorker>()
            .build()
        WorkManager.getInstance().enqueueUniqueWork("database", ExistingWorkPolicy.REPLACE, uploadWorkRequest)

    }
}