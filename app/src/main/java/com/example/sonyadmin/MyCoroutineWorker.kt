package com.example.sonyadmin

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.work.*
import com.example.sonyadmin.data.DailyCount
import com.example.sonyadmin.data.Repository.Repository
import com.example.sonyadmin.data.Task
import com.example.sonyadmin.gameList.Model.countGameSum
import com.example.sonyadmin.gameList.Model.countTotalSum
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.processphoenix.ProcessPhoenix
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import org.joda.time.DateTime
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.TimeUnit


const val hour = 9
const val time = hour * 60 * 60

class MyCoroutineWorker(val context: Context, params: WorkerParameters) : CoroutineWorker(context, params)
    , KoinComponent {

    override val coroutineContext = Dispatchers.IO
    val repository: Repository by inject()
    lateinit var userName: String

    override suspend fun doWork(): Result = coroutineScope {
        userName = FirebaseAuth.getInstance().currentUser!!.email!!.substringBeforeLast("@")
        val last = repository.getLastCash()

        if (last == null) {
            repository.setCash(DailyCount(DateTime.now(), userName, DateTime.now().dayOfYear, 0.0))
            return@coroutineScope Result.success()
        }
        val lastTimeFromDB = last.date
        val currentDateTime = DateTime.now()
        if (!isWorkingDayFinished(
                lastTimeFromDB,
                currentDateTime
            )
        ) {
            var timediff: Int
            Log.d("Worker", "yea it true ${last}")

            //if time >9
            if (DateTime.now().secondOfDay > time) {
                timediff = (24 + hour) * 60 * 60 - currentDateTime.secondOfDay
            } else {
                timediff = time - currentDateTime.secondOfDay
            }
            Log.d("Worker", "${timediff / 60 / 60}")
            val dailyWorkRequest = OneTimeWorkRequestBuilder<MyCoroutineWorker>()
                .setInitialDelay(timediff.toLong(), TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance().enqueueUniqueWork("database", ExistingWorkPolicy.REPLACE, dailyWorkRequest)

        } else {
            Log.d("Worker", "nein it falsh ${last}")
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed(Runnable {
                // Run your task here
                Toast.makeText(context, "09:00 refresh", Toast.LENGTH_LONG).show()
            }, 1000)
            // I need to finish all started games for previous game and continue count as it was started today
            for (x in 0..10) {
                var lastTask = repository.getLastGame(x)
                if (lastTask!!.isPlaying) {
                    val summOfTheGame = countGameSum(lastTask, DateTime.now())
                    val summWithBar = countTotalSum(lastTask, DateTime.now())
                    repository.writeEndTime(
                        Task(
                            id = lastTask!!.id, startTime = lastTask.startTime, userName = userName,
                            endTime = currentDateTime, cabinId = x, summOfTheGame = summOfTheGame,
                            isPlaying = false,
                            totalSumWithBar = summWithBar
                            ,listOfBars = ArrayList()
                        )
                    )


                    repository.updateCash(
                        lastTimeFromDB.minusDays(1),
                        currentDateTime.plusDays(1).withTime(23, 59, 59, 0),
                        lastTimeFromDB.dayOfYear,
                        summOfTheGame
                    )
                    repository.writeStartTime(
                        Task(
                            startTime = currentDateTime,
                            cabinId = x,
                            isPlaying = true,
                            userName = userName,
                            listOfBars = ArrayList()
                        )
                    )
                }

            }
            repository.setCash(DailyCount(currentDateTime, userName, DateTime.now().dayOfYear, 0.0))
            Log.d("Worker", "no it is not ${last}")
            Log.d("Worker", " rebearth ${last}")
            var timediff: Int
            Log.d("Worker", "yea it true ${last}")

            //if time >9
            if (DateTime.now().secondOfDay > time) {
                timediff = (24 + hour) * 60 * 60 - currentDateTime.secondOfDay
            } else {
                timediff = time - currentDateTime.secondOfDay
            }
            Log.d("Worker", "${timediff / 60 / 60}")
            val dailyWorkRequest = OneTimeWorkRequestBuilder<MyCoroutineWorker>()
                .setInitialDelay(timediff.toLong(), TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance().enqueueUniqueWork("database", ExistingWorkPolicy.REPLACE, dailyWorkRequest)

            ProcessPhoenix.triggerRebirth(context)
        }


        Result.success()
    }
}

fun isWorkingDayFinished(lastDate: DateTime, currentDateTime: DateTime): Boolean {

    //if lastdate <= previous day and now >= 09:00
    if (lastDate.dayOfYear <= currentDateTime.minusDays(1).dayOfYear && currentDateTime.secondOfDay >= time) {
        return true//finish
    }
    //lastdate = today and now > 09:00
    if (lastDate.dayOfYear == currentDateTime.dayOfYear && currentDateTime.secondOfDay > time) {
        if (lastDate.hourOfDay < 9 && currentDateTime.secondOfDay > time) {
            return true
        }
        return false//continue
    }
    if (lastDate.dayOfYear == currentDateTime.dayOfYear && currentDateTime.secondOfDay == time) {
        if (lastDate.secondOfDay == time) {
            return false//continue
        } else {
            return true//finish
        }
    }
    //lastdate = today and now < 09:00
    if (lastDate.dayOfYear == currentDateTime.dayOfYear && currentDateTime.secondOfDay < time) {
        return false//continue
    }
    //if lastdate == previous day and now < 09:00
    if (lastDate.dayOfYear == currentDateTime.minusDays(1).dayOfYear && currentDateTime.secondOfDay < time) {
        return false//continue
    }
//    //lastdate = today and lastTime > 09:00
//    if (lastDate.dayOfYear==currentDateTime.dayOfYear && lastDate.minuteOfDay>540){
//        return false//continue
//    }
    else {
        throw Exception()
    }
}

