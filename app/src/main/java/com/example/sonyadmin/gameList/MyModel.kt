package com.example.sonyadmin.gameList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sonyadmin.EveryDayUpdateCashWorker
import com.example.sonyadmin.data.Repository.Repository
import com.example.sonyadmin.data.Task
import com.example.sonyadmin.data.service.Api
import com.example.sonyadmin.gameList.Model.*
import com.example.sonyadmin.util.Event
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.joda.time.DateTime

class MyModel(
    var repository: Repository,
    application: Application,
    val userService: Api,
    everyDayUpdateCashWorker: EveryDayUpdateCashWorker
) :
    AndroidViewModel(application),
    CoroutineScope by MainScope() {

    internal val _showToast = MutableLiveData<Event<String>>()
    lateinit var userName: String

    val showToast: LiveData<Event<String>>
        get() = _showToast

    val items = MutableLiveData<List<LiveData<Task>>>().apply {
        value = emptyList()
    }
    val liveItems = repository.list
    val dataLoading = MutableLiveData<Boolean>(false)

    init {
        userName = FirebaseAuth.getInstance().currentUser!!.email!!.substringBeforeLast("@")
        Log.d("init", userName)
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
                    DateTime.now().minusDays(1).withTime(0, 0, 0, 0),
                    DateTime.now().plusDays(1).withTime(23, 59, 59, 0),
                    determineDay(),
                    countSum(task, DateTime.now())
                )
            }
        )

    }


    private val TAG: String = MyModel::class.java.simpleName

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