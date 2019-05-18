package com.example.sonyadmin.gameList

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.sonyadmin.MyCoroutineWorker
import com.example.sonyadmin.data.Repository
import com.example.sonyadmin.data.Task
import com.example.sonyadmin.gameList.Model.changeGameEndTime
import com.example.sonyadmin.gameList.Model.changeGameStartTime
import com.example.sonyadmin.gameList.Model.onBG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

class MyModel(var repository: Repository,application: Application) : AndroidViewModel(application), CoroutineScope by MainScope(),ScopeProvider {
    override fun provideScope(): CoroutineScope {
        return viewModelScope
    }


    val liveItems =repository.list
    val dataLoading = MutableLiveData<Boolean>(false)

    init {
//        liveItems.observeForever {
//            Log.e("Onclick","yeee llistLive changed ${it}")
//        }
        liveItems.forEach {
            it.observeForever {
                Log.d("Onclick","observer for ${it}")
            }
        }
        val uploadWorkRequest = OneTimeWorkRequestBuilder<MyCoroutineWorker>()
            .build()
        WorkManager.getInstance().enqueueUniqueWork("database", ExistingWorkPolicy.REPLACE, uploadWorkRequest)

    }

    fun completeTask(task: Task) {

        Log.d("Onclick","onClick complete ${task}")
        onBG { repository.writeEndTime( changeGameEndTime(task)) }
    }

    fun openTask(task: Task) {
        Log.d("Onclick","onClick open ${task}")
        onBG {
            repository.writeStartTime(changeGameStartTime(task))
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