package com.example.sonyadmin.gameList.Model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sonyadmin.data.Task
import com.example.sonyadmin.gameList.MyModel
import kotlinx.coroutines.*
import org.joda.time.DateTime
import org.joda.time.Duration
import java.lang.Thread.sleep
import java.math.RoundingMode


fun MyModel.changeGameEndTime(task: Task): Task {
    return Task(
        cabinId = task.cabinId, endTime = DateTime.now(),
        isPlaying = false, startTime = task.startTime, summ = countMinutes(task),id = task.id
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


fun MyModel.onBG(bar: suspend () -> Unit) {
    launch {
        withContext(Dispatchers.IO) {
            bar()
        }

    }
}