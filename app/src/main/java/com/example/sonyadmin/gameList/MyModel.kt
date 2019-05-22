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
import com.example.sonyadmin.data.service.UserRepository
import com.example.sonyadmin.gameList.Model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import me.nikhilchaudhari.asynkio.core.async
import me.nikhilchaudhari.asynkio.core.request
import me.nikhilchaudhari.asynkio.response.Response
import org.joda.time.DateTime

class MyModel(var repository: Repository, application: Application, val userRepository: UserRepository) :
    AndroidViewModel(application),
    CoroutineScope by MainScope(), ScopeProvider {
    override fun provideScope(): CoroutineScope {
        return viewModelScope
    }

    private val _showToast = MutableLiveData<Event<String>>()

    val showToast: LiveData<Event<String>>
        get() = _showToast

    val items = MutableLiveData<List<LiveData<Task>>>().apply {
        value = emptyList()
    }
    val liveItems =repository.list
    val dataLoading = MutableLiveData<Boolean>(false)
    val anyData = MutableLiveData<String>("hz")

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
//        launch {
//            val h = userRepository.search()
//            if (h.isSuccessful)
//            Log.d("Retrofit","eeeeee Retrofit ${h}")
//        }
        sendRequest(task, dataLoading,"off", _showToast) {
            repository.writeEndTime(changeGameEndTime(task))
            repository.updateCash( DateTime.now().withTime(0, 0, 0, 0), DateTime.now().withTime(23, 59, 59, 0),
                countMinutes(task))
        }
    }


    fun openTask(task: Task) =viewModelScope.launch{
//            val h = userRepository.search().body()
//        repository.writeStartTime(changeGameStartTime(task))
        sendRequest(task, dataLoading,"onn", _showToast) { repository.writeStartTime(changeGameStartTime(task)) }
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