package com.example.sonyadmin.gameList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.sonyadmin.Event
import com.example.sonyadmin.MyCoroutineWorker
import com.example.sonyadmin.data.Repository
import com.example.sonyadmin.data.Task
import com.example.sonyadmin.gameList.Model.*
import io.navendra.retrofitkotlindeferred.service.UserService
import kotlinx.coroutines.*
import org.joda.time.DateTime

class MyModel(var repository: Repository, application: Application, val userService: UserService) :
    AndroidViewModel(application),
    CoroutineScope by MainScope(), ScopeProvider {
    override fun provideScope(): CoroutineScope {
        return viewModelScope
    }

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
        val uploadWorkRequest = OneTimeWorkRequestBuilder<MyCoroutineWorker>()
            .build()
        WorkManager.getInstance().enqueueUniqueWork("database", ExistingWorkPolicy.REPLACE, uploadWorkRequest)

    }

    fun completeTask(task: Task) {
        makeRequest( {
            userService.off(task.cabinId.toString()).await()
        }, {
            repository.writeEndTime(changeGameEndTime(task))
            repository.updateCash(
                DateTime.now().withTime(0, 0, 0, 0), DateTime.now().withTime(23, 59, 59, 0),
                countSum(task, DateTime.now())
            )
        })

    }


    fun openTask(task: Task)  {
        makeRequest( {userService.onn(task.cabinId.toString()).await()}, {
            repository.writeStartTime(changeGameStartTime(task))
        })

    }

    fun deleteAll() {
        repository.deleteAll()
    }
}