package com.example.sonyadmin.gameList

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
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
import com.example.sonyadmin.gameList.Model.changeGameEndTime
import com.example.sonyadmin.gameList.Model.changeGameStartTime
import com.example.sonyadmin.gameList.Model.onBG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import me.nikhilchaudhari.asynkio.core.async
import me.nikhilchaudhari.asynkio.core.request
import me.nikhilchaudhari.asynkio.response.Response

class MyModel(var repository: Repository,application: Application) : AndroidViewModel(application), CoroutineScope by MainScope(),ScopeProvider {
    override fun provideScope(): CoroutineScope {
        return viewModelScope
    }
    private val _showToast = MutableLiveData<Event<String>>()

    val showToast : LiveData<Event<String>>
        get() = _showToast

    val items = MutableLiveData<List<LiveData<Task>>>().apply {
        value = emptyList()
    }
    val liveItems =repository.list
    val dataLoading = MutableLiveData<Boolean>(false)

    init {
        liveItems.forEach {
            it.observeForever {
                items.postValue(liveItems)
                Log.d("Onclick","observer for ${it}")
            }
        }
        val uploadWorkRequest = OneTimeWorkRequestBuilder<MyCoroutineWorker>()
            .build()
        WorkManager.getInstance().enqueueUniqueWork("database", ExistingWorkPolicy.REPLACE, uploadWorkRequest)

    }

    fun completeTask(task: Task) {
        if (isInternetOn(getApplication())) {
            dataLoading.postValue(true)
            async {
                lateinit var result: Response
                try {

                    result = await {
                        request(
                            method = "GET",
                            url = "https://jsonplaceholder.typicode.com/posts/${task.cabinId}",
                            timeout = 1.0
                        )
                    }
                } catch (exeption: Exception) {
                    _showToast.value = Event("ошибка ${exeption.toString()}")
                    dataLoading.postValue(false)
                }
                if (result.statusCode == 200) {
                    onBG {
                        repository.writeEndTime(changeGameEndTime(task))
                        dataLoading.postValue(false)
                    }
                } else {
                    _showToast.value = Event("нет соединения с сервером")
                    dataLoading.postValue(false)
                }
            }.onError {
                dataLoading.postValue(false)
            }
        }else{
            _showToast.value = Event("no internet")
        }
    }

    fun openTask(task: Task) {
        if (isInternetOn(getApplication())) {
            dataLoading.value = true
            async {
                lateinit var result: Response
                try {

                    result = await {
                        request(
                            method = "GET",
                            url = "https://jsonplaceholder.typicode.com/posts/${task.cabinId}",
                            timeout = 1.0
                        )
                    }
                } catch (exeption: Exception) {
                    _showToast.value = Event("ошибка ${exeption.toString()}")
                }
                if (result.statusCode == 200) {
                    dataLoading.postValue(false)
                    onBG {
                        repository.writeStartTime(changeGameStartTime(task))
                    }
                } else {
                    _showToast.value = Event("нет соединения с сервером")
                    dataLoading.postValue(false)
                }
            }.onError {
                dataLoading.postValue(false)
                _showToast.value = Event("error")
            }
        }else{
            _showToast.value = Event("no internet")
        }
    }

    fun deleteAll() {
        repository.deleteAll()
    }

    fun isInternetOn(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val activeNetwork = cm?.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}