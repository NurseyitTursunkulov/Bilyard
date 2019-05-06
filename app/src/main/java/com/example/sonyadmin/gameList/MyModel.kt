package com.example.sonyadmin.gameList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sonyadmin.data.Repository
import com.example.sonyadmin.data.Task
import com.example.sonyadmin.gameList.Model.changeGameEndTime
import com.example.sonyadmin.gameList.Model.changeGameStartTime
import com.example.sonyadmin.gameList.Model.initItems
import com.example.sonyadmin.gameList.Model.onBG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import me.nikhilchaudhari.asynkio.core.async
import me.nikhilchaudhari.asynkio.core.request
import me.nikhilchaudhari.asynkio.response.Response

class MyModel(var repository: Repository) : ViewModel(), CoroutineScope by MainScope() {
    val items = MutableLiveData<List<MutableLiveData<Task>>>().apply {
        value = emptyList()
    }
    val dataLoading = MutableLiveData<Boolean>()

    init {
        initItems()
    }

    fun completeTask(task: Task) {
        changeGameEndTime(items.value!![task.cabinId].value!!)
        onBG {
            async {
                lateinit var result: Response
                try {

                     result = await {
                        request(method = "GET", url = "https://jsonplaceholder.typicode.com/posts/${task.cabinId}", timeout = 1.0)
                    }
                    Log.d("Internet", "${result.jsonObject}")
                    Log.d("Internet", "status ${result.statusCode}")
                } catch (exeption: Exception) {
                    Log.d("Internet", "status ${result.statusCode}")
                }
                if (result.statusCode == 200){
                    Log.d("Internet", "status okk ${result.statusCode}")
                }
            }.onError {

            }

            repository.writeEndTime(items.value!![task.cabinId].value!!)
        }
    }

    fun openTask(task: Task) {
        changeGameStartTime(task)
        onBG {
            async {
                lateinit var result: Response
                try {

                    result = await {
                        request(method = "GET", url = "https://jsonplaceholder.typicode.com/posts/${task.cabinId}", timeout = 1.0)
                    }
                    Log.d("Internet", "${result.jsonArray.get(1)}")
                    Log.d("Internet", "status ${result.statusCode}")
                } catch (exeption: Exception) {
                    Log.d("Internet", "exeption $exeption")
                    Log.d("Internet", "status ${result.statusCode}")

                }

            }
            repository.writeStartTime(items.value!![task.cabinId].value!!)
            items.value!![task.cabinId].postValue(repository.getLastGame(task.cabinId)?.value!!)
        }

    }

    fun deleteAll() {
        repository.deleteAll()
    }


}