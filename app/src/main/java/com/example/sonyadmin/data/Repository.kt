package com.example.sonyadmin.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource


interface Repository {
    fun writeStartTime(game: Task)

    fun writeEndTime(game: Task)

    fun getAllGameProccesBiCabin(cabinId: Int): LiveData<List<Task>>?

    fun getLastGame(cabinId: Int): MutableLiveData<Task>?

    fun deleteAll()

    fun getGameDetails(cabinId: Int): DataSource.Factory<Int, Task>
}