package com.example.sonyadmin

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.sonyadmin.data.DailyCount
import com.example.sonyadmin.data.Dao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import net.danlew.android.joda.DateUtils
import org.joda.time.DateTime
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.TimeUnit

class MyCoroutineWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params)
    , KoinComponent {
    override val coroutineContext = Dispatchers.IO
    val dao: Dao by inject()
    override suspend fun doWork(): Result = coroutineScope {

        val last = dao.getLastCash()

        if (last == null) {
            dao.setCash(DailyCount(DateTime.now(), 0.0))
        }
        if (DateUtils.isToday(last?.date)) {
            Log.d("Worker", "yea it true ${last}")
            Log.d("Worker", "${last?.date?.minuteOfDay}")
            val timediff = 86400 - last?.date?.secondOfDay!!
            val dailyWorkRequest = OneTimeWorkRequestBuilder<MyCoroutineWorker>()
                .setInitialDelay(timediff.toLong(), TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance().enqueueUniqueWork("database", ExistingWorkPolicy.REPLACE, dailyWorkRequest)

        } else {
            dao.setCash(DailyCount(DateTime.now(), 0.0))
            Log.d("Worker", "no it is not ${last}")
        }


        Result.success()
    }
}