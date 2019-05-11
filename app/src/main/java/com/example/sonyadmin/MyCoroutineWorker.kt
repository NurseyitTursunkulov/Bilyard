package com.example.sonyadmin

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.sonyadmin.data.DailyCount
import com.example.sonyadmin.data.Dao
import com.example.sonyadmin.data.Repository
import com.example.sonyadmin.gameList.Model.countMinutes
import com.example.sonyadmin.gameList.MyModel
import com.jakewharton.processphoenix.ProcessPhoenix
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import net.danlew.android.joda.DateUtils
import org.joda.time.DateTime
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.TimeUnit
import org.koin.android.viewmodel.ext.android.sharedViewModel

class MyCoroutineWorker(val context: Context, params: WorkerParameters) : CoroutineWorker(context, params)
    , KoinComponent {
    override val coroutineContext = Dispatchers.IO
    val repository: Repository by inject()
    override suspend fun doWork(): Result = coroutineScope {

        val last = repository.getLastCash()

        if (last == null) {
            repository.setCash(DailyCount(DateTime.now(), 0.0))
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
            // I need to finish all started games for previous game and continue count as it was started today
            for (x in 0..10){
                repository.getLastGame(x)?.value.apply {
                    if (this!!.isPlaying.value == true){
                        val duration = countMinutes(this)
                        this.endTime?.value = DateTime.now()
                        this.summ?.value = duration
                        this.isPlaying?.value = false
                        repository.writeEndTime(this)
                        repository.updateCash(DateTime.now().minusDays(1).
                            withTime(0,0,0,0),DateTime.now().withTime(23,59,59,0),duration)
                        this.startTime.value = DateTime.now()
                        this.isPlaying.value = true
                        repository.writeStartTime(this)
                    }
                }

            }
            repository.setCash(DailyCount(DateTime.now(), 0.0))
            Log.d("Worker", "no it is not ${last}")
            ProcessPhoenix.triggerRebirth(context)
        }


        Result.success()
    }
}