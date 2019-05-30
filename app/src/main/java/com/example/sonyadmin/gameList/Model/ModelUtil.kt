package com.example.sonyadmin.gameList.Model

import android.util.Log
import com.example.sonyadmin.util.Event
import com.example.sonyadmin.data.Result
import com.example.sonyadmin.data.Task
import com.example.sonyadmin.data.service.PlaceholderPosts
import com.example.sonyadmin.gameList.MyModel
import kotlinx.coroutines.*
import org.joda.time.*
import java.math.RoundingMode


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
private fun countMinutes(task: Task, endTime: DateTime):Double{
    var duration: Double = (Duration(task.startTime, endTime).standardSeconds) / 60.toDouble()
    return duration.toBigDecimal().setScale(0, RoundingMode.UP).toDouble()
}

fun MyModel.countSum(task: Task, endTime: DateTime): Double {
    var minutes = countMinutes(task,endTime)
    if (task.cabinId!=1) {
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
        if (fomMiddNightTillMiddDay && checkInterval(12, 17, endTime)) {
            var firstSum: Double = countFirstTime(task, 12, 0)
            var secondTime: Double = countSecondTime(12, 0, endTime)
            sum = (firstSum / 60 * 100) + (secondTime / 60 * 150)
        }
        if (fromMiddDayTillEvening && checkInterval(17, 23, endTime)) {
            var firstSum: Double = countFirstTime(task, 18, 0)
            var secondTime: Double = countSecondTime(18, 0, endTime)
            sum = (firstSum / 60 * 150) + (secondTime / 60 * 180)
        }
        if (fromEveningTillMiddNight && checkIntervalForNextDay(0, 11, endTime)) {
            var firstSum: Double = countFirstTime(task, 0, 0)
            var secondTime: Double = countSecondTime(0, 0, endTime)
            sum = (firstSum + secondTime)/60*180
        }
        return sum.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
    }
    else{
        var fromMorningTillEvening = checkInterval(9, 17, task.startTime)
        var sum: Double = minutes / 60 * 200
        if (fromMorningTillEvening) {
            sum = minutes / 60 * 200
        }
        var fromEveningTillMiddNight = checkInterval(18, 23, task.startTime)

        if (fromEveningTillMiddNight) {
            sum = minutes / 60 * 250
        }

        var fromMiddNightTillMorning = checkInterval(0, 8, task.startTime)

        if (fromEveningTillMiddNight || fromMiddNightTillMorning) {
            sum = minutes / 60 * 250
        }

        /***/
        if (fromMorningTillEvening && checkInterval(18, 23, endTime)) {
            var firstSum: Double = countFirstTime(task, 18, 0)
            var secondTime: Double = countSecondTime(18, 0, endTime)
            sum = (firstSum / 60 * 200) + (secondTime / 60 * 250)
        }
        return sum.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
    }
}

private fun countSecondTime(hour: Int, minute: Int, endTime: DateTime): Double {
    return (Duration(
        DateTime.now().withHourOfDay(hour).withMinuteOfHour(minute),endTime).standardSeconds) / 60.toDouble()
}

private fun countFirstTime(task: Task, hour: Int, minute: Int): Double {
    return (Duration(
        task.startTime,
        DateTime.now().withHourOfDay(hour).withMinuteOfHour(minute)
    ).standardSeconds) / 60.toDouble()
}

fun checkInterval(startTime: Int, endTime: Int, inRange: DateTime): Boolean {
    return Interval(DateTime.now().withHourOfDay(startTime).withMinuteOfHour(0).withSecondOfMinute(0),
        DateTime.now().withHourOfDay(endTime).withMinuteOfHour(59).withSecondOfMinute(59)).contains(inRange)
}
fun checkIntervalForNextDay(startTime: Int, endTime: Int, inRange: DateTime): Boolean {
    return Interval(DateTime.now().plusDays(1).withHourOfDay(startTime).withMinuteOfHour(0).withSecondOfMinute(0),
        DateTime.now().plusDays(1).withHourOfDay(endTime).withMinuteOfHour(59).withSecondOfMinute(59)).contains(inRange)
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