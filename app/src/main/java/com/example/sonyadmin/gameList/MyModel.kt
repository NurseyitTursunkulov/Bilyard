package com.example.sonyadmin.gameList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.sonyadmin.EveryDayUpdateCashWorker
import com.example.sonyadmin.util.Event
import com.example.sonyadmin.MyCoroutineWorker
import com.example.sonyadmin.data.Repository
import com.example.sonyadmin.data.Task
import com.example.sonyadmin.data.service.Api
import com.example.sonyadmin.gameList.Model.*
import kotlinx.coroutines.*
import org.joda.time.DateTime
import org.koin.core.KoinComponent
import org.koin.core.inject

class MyModel(var repository: Repository, application: Application, val userService: Api, everyDayUpdateCashWorker: EveryDayUpdateCashWorker ) :
    AndroidViewModel(application),
    CoroutineScope by MainScope() {

    internal val _showToast = MutableLiveData<Event<String>>()

    val showToast: LiveData<Event<String>>
        get() = _showToast

    val items = MutableLiveData<List<LiveData<Task>>>().apply {
        value = emptyList()
    }
    val liveItems = repository.list
    val dataLoading = MutableLiveData<Boolean>(false)

    init {
        liveItems.forEach {
            it.observeForever {
                items.postValue(liveItems)
                Log.d("Onclick", "observer for ${it}")
            }
        }
        everyDayUpdateCashWorker.update()
    }

    fun completeTask(task: Task) {
        makeRequest(
            { userService.off(task.cabinId.toString()) },
            {
                repository.writeEndTime(changeGameEndTime(task))
                repository.updateCash(
                    DateTime.now().withTime(0, 0, 0, 0), DateTime.now().withTime(23, 59, 59, 0),
                    countSum(task, DateTime.now())
                )
            }
        )

    }


    fun openTask(task: Task) {
        makeRequest(
            { userService.onn(task.cabinId.toString()) },
            {
                repository.writeStartTime(changeGameStartTime(task))
            }
        )

    }

    fun deleteAll() {
        repository.deleteAll()
    }
}