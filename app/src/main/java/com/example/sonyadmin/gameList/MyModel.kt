package com.example.sonyadmin.gameList

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

class MyModel(var repository: Repository) : ViewModel(), CoroutineScope by MainScope() {
    val items = MutableLiveData<List<MutableLiveData<Task>>>().apply {
        value = emptyList()
    }

    init {
        initItems()
    }

    fun completeTask(task: Task) {
        changeGameEndTime(task)
        onBG { repository.writeEndTime(task) }
    }

    fun openTask(task: Task) {
        changeGameStartTime(task)
        onBG {
            repository.writeStartTime(items.value!![task.cabinId].value!!)
        }

    }

    fun deleteAll() {
        repository.deleteAll()
    }



}