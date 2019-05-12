package com.example.sonyadmin.gameList

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.sonyadmin.MyCoroutineWorker
import com.example.sonyadmin.data.DailyCount
import com.example.sonyadmin.data.Repository
import com.example.sonyadmin.data.Task
import com.example.sonyadmin.gameList.Model.changeGameEndTime
import com.example.sonyadmin.gameList.Model.changeGameStartTime
import com.example.sonyadmin.gameList.Model.initItems
import com.example.sonyadmin.gameList.Model.onBG
import io.navendra.retrofitkotlindeferred.service.ApiFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import me.nikhilchaudhari.asynkio.core.async
import me.nikhilchaudhari.asynkio.core.request
import me.nikhilchaudhari.asynkio.response.Response
import net.danlew.android.joda.DateUtils
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.text.SimpleDateFormat

class MyModel(var repository: Repository,application: Application) : AndroidViewModel(application), CoroutineScope by MainScope() {
    val items = MutableLiveData<List<MutableLiveData<Task>>>().apply {
        value = emptyList()
    }
    val dataLoading = MutableLiveData<Boolean>(false)

    init {
        val uploadWorkRequest = OneTimeWorkRequestBuilder<MyCoroutineWorker>()
            .build()
        WorkManager.getInstance().enqueueUniqueWork("database", ExistingWorkPolicy.REPLACE, uploadWorkRequest)
        initItems()
    }

    fun completeTask(task: Task) {
        changeGameEndTime(items.value!![task.cabinId].value!!)
        onBG {
            val response = ApiFactory.placeholderApi.off().await()
            if(response.isSuccessful){
                Log.d("Retrofit", response.body().toString())
            }
            else{
                Log.d("Retrofit", "else complete")
            }
            items.value!![task.cabinId].value?.summ?.value?.apply {
                repository.updateCash(
                    DateTime.now().withTime(0, 0, 0, 0), DateTime.now().withTime(23, 59, 59, 0),
                    this
                )
            }

            repository.writeEndTime(items.value!![task.cabinId].value!!)
        }
    }

    fun openTask(task: Task) {
        changeGameStartTime(task)
        onBG {
            val response = ApiFactory.placeholderApi.onn().await()
            if(response.isSuccessful){
                Log.d("Retrofit", response.body().toString())
            }
            else{
                Log.d("Retrofit", "else open")
            }
            repository.writeStartTime(items.value!![task.cabinId].value!!)
            items.value!![task.cabinId].postValue(repository.getLastGame(task.cabinId)?.value!!)
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