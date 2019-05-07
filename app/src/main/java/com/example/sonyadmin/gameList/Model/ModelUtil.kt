package com.example.sonyadmin.gameList.Model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.sonyadmin.data.Task
import com.example.sonyadmin.gameList.MyModel
import kotlinx.coroutines.*
import org.joda.time.DateTime
import org.joda.time.Duration
import java.lang.Thread.sleep
import java.math.RoundingMode

fun MyModel.initItems() {
    launch {
        dataLoading.postValue(true)
        var list = arrayListOf<MutableLiveData<Task>>()
        withContext(Dispatchers.IO) { // l
            for (x  in 0..10){
                list.add(repository.getLastGame(x) ?: MutableLiveData<Task>(Task(cabinId = x)))
                Log.d("Init","in for $x")
            }
            Log.d("Init","after")
            list.sortBy {
                it.value?.cabinId
            }
            Log.d("Init","after sort")
            items.postValue(list)
            dataLoading.postValue(false)
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