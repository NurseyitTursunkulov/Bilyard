package com.example.sonyadmin.gameList.Model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.sonyadmin.Event
import com.example.sonyadmin.data.Result
import com.example.sonyadmin.data.Task
import com.example.sonyadmin.data.service.PlaceholderPosts
import com.example.sonyadmin.gameList.MyModel
import kotlinx.coroutines.*
import me.nikhilchaudhari.asynkio.core.async
import me.nikhilchaudhari.asynkio.core.request
import me.nikhilchaudhari.asynkio.response.Response
import org.joda.time.*
import java.lang.Exception
import java.math.RoundingMode
import java.net.ConnectException
import java.net.SocketTimeoutException
import kotlin.math.roundToInt


fun MyModel.changeGameEndTime(task: Task): Task {
    return Task(
        cabinId = task.cabinId, endTime = DateTime.now(),
        isPlaying = false, startTime = task.startTime, summ = countSum(task, DateTime.now()), id = task.id
    )
}

fun countMinutes(task: Task): Double {
    var duration: Double = (Duration(task.startTime, DateTime.now()).standardSeconds) / 60.toDouble()
    duration = duration.toBigDecimal().setScale(0, RoundingMode.UP).toDouble()
    return duration
}

fun MyModel.changeGameStartTime(task: Task): Task {
    return Task(
        cabinId = task.cabinId, startTime = DateTime.now(),
        isPlaying = true, endTime = null
    )
}


fun countSum(task: Task, endTime: DateTime): Double {
    var minutes = countMinutes(task)
    var sum: Double = minutes / 60 * 100
    var fomMiddNightTillMiddDay = checkInterval(0, 11, task.startTime)
    Log.d("Main", "interval = $fomMiddNightTillMiddDay")
    if (fomMiddNightTillMiddDay) {
        sum = minutes / 60 * 100
    }
    var fromMiddDayTillEvening = checkInterval(12, 17, task.startTime)
    if (fromMiddDayTillEvening) {
        sum = minutes / 60 * 150
    }
    var fromEveningTillMiddNight = checkInterval(17, 23, task.startTime)

    if (fromEveningTillMiddNight) {
        sum = minutes / 60 * 180
    }
    /***/
    if (fomMiddNightTillMiddDay && checkInterval(12, 17, DateTime.now())) {
        var firstSum: Double = countFirstTime(task, 12, 0)
        var secondTime: Double = countSecondTime(12, 0)
        sum = (firstSum / 60 * 100) + (secondTime / 60 * 150)
    }
    if (fromMiddDayTillEvening && checkInterval(17, 23, DateTime.now())) {
        var firstSum: Double = countFirstTime(task, 18, 0)
        var secondTime: Double = countSecondTime(18, 0)
        sum = (firstSum / 60 * 150) + (secondTime / 60 * 180)
    }
    if (fromEveningTillMiddNight && checkInterval(0, 11, DateTime.now())) {
        var firstSum: Double = countFirstTime(task, 0, 0)
        var secondTime: Double = countSecondTime(0, 0)
        sum = firstSum + secondTime
    }
    return sum.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
}

private fun countSecondTime(hour: Int, minute: Int): Double {
    return (Duration(
        DateTime.now().withHourOfDay(hour).withMinuteOfHour(minute),
        DateTime.now()
    ).standardSeconds) / 60.toDouble()
}

private fun countFirstTime(task: Task, hour: Int, minute: Int): Double {
    return (Duration(
        task.startTime,
        DateTime.now().withHourOfDay(hour).withMinuteOfHour(minute)
    ).standardSeconds) / 60.toDouble()
}

fun checkInterval(startTime: Int, endTime: Int, inRange: DateTime): Boolean {
    return Interval(DateTime.now().withHourOfDay(startTime), DateTime.now().withHourOfDay(endTime)).contains(inRange)
}

fun MyModel.onBG(bar: suspend () -> Unit) {
    launch {
        withContext(Dispatchers.IO) {
            bar()
        }

    }
}

fun MyModel.makeRequest(
    bar: suspend () -> Result<PlaceholderPosts>, onSuccess:
        () -> Unit
) {
    dataLoading.postValue(true)
    launch {
        withContext(Dispatchers.IO) {
            try {
                val h = bar()
                if (h is Result.Success) {
                    onSuccess()
                    Log.d("Retrofit", "eeeeee Retrofit ${h}")
                } else {
                    _showToast.postValue(Event(" нет соединения"))
                    Log.d("Retrofit", "uuuuu Retrofit ${h}")
                }
            }finally {
                dataLoading.postValue(false)
            }
        }

    }
}