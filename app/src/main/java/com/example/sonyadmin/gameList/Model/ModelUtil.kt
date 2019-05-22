package com.example.sonyadmin.gameList.Model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sonyadmin.Event
import com.example.sonyadmin.data.Task
import com.example.sonyadmin.gameList.MyModel
import kotlinx.coroutines.*
import me.nikhilchaudhari.asynkio.core.async
import me.nikhilchaudhari.asynkio.core.request
import me.nikhilchaudhari.asynkio.response.Response
import org.joda.time.DateTime
import org.joda.time.Duration
import java.lang.Thread.sleep
import java.math.RoundingMode
import java.net.ConnectException
import java.net.SocketTimeoutException


fun MyModel.changeGameEndTime(task: Task): Task {
    return Task(
        cabinId = task.cabinId, endTime = DateTime.now(),
        isPlaying = false, startTime = task.startTime, summ = countMinutes(task), id = task.id
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

fun MyModel.sendRequest(
    task: Task, dataLoading: MutableLiveData<Boolean>,
    state: String,
    _showToast: MutableLiveData<Event<String>>,
    bar: () -> Unit
) {
    if (isInternetOn(getApplication())) {
        dataLoading.postValue(true)
        async {
            lateinit var result: Response
            try {

                result = await {
                    request(
                        method = "GET",
                        url = "http://192.168.43.9:5000/${state}/${task.cabinId}",
                        timeout = 3.0
                    )
                }
            } catch (connect: ConnectException) {
                _showToast.value = Event(" нет соединения")
                dataLoading.postValue(false)
            } catch (exeption: SocketTimeoutException) {
                _showToast.value = Event("нет соединения")
                dataLoading.postValue(false)
            } catch (exeption: Exception) {
                dataLoading.postValue(false)
                _showToast.value = Event("ошибка ${exeption.toString()}")
            }
            if (result.statusCode == 200) {
                onBG {
                    bar()
                    dataLoading.postValue(false)
                }
            } else {
                _showToast.value = Event("нет соединения с сервером")
                dataLoading.postValue(false)
            }
        }.onError {
            dataLoading.postValue(false)
            _showToast.value = Event("ошибка ${it.toString()}")
        }
    } else {
        _showToast.value = Event("no internet")
    }
}