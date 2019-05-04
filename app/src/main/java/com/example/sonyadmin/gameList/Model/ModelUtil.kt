package com.example.sonyadmin.gameList.Model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.sonyadmin.data.Task
import com.example.sonyadmin.gameList.MyModel
import kotlinx.coroutines.*
import org.joda.time.DateTime
import org.joda.time.Duration
import java.math.RoundingMode

fun MyModel.initItems() {
    launch {
        var list = arrayListOf<MutableLiveData<Task>>()
        coroutineScope { // limits the scope of concurrency
            (0..10).map {
                async(Dispatchers.IO) { // async means "concurrently", context goes here
                    list.add(repository.getLastGame(it) ?: MutableLiveData<Task>(Task(cabinId = it)))
                    Log.d("Init","async $it")
                }
            }.awaitAll() // waits all of them
            Log.d("Init","after")
            list.sortBy {
                it.value?.cabinId
            }
            Log.d("Init","after sort")
            items.postValue(list)

        } // if any task crashes -- this scope ends with exception

    }
}

fun MyModel.changeGameEndTime(task: Task) {
    items.value!![task.cabinId].value?.apply {
        var duration: Double = (Duration(this.startTime.value, DateTime.now()).standardSeconds) / 60.toDouble()
        duration = duration.toBigDecimal().setScale(0, RoundingMode.UP).toDouble()

        this.endTime?.value = DateTime.now()
        this.summ?.value = duration
        this.isPlaying?.value = false
    }

}

fun MyModel.changeGameStartTime(task: Task) {
    items.value!![task.cabinId].value = Task(cabinId = task.cabinId).apply {
        startTime.value = DateTime.now()
        isPlaying.value = true
    }
}

fun MyModel.onBG(bar: suspend () -> Unit) {
    launch {
        withContext(Dispatchers.IO) {
            bar()
        }

    }
}