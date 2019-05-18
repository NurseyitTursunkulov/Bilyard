package com.example.sonyadmin.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import org.joda.time.DateTime


interface Repository {
    var list:ArrayList<LiveData<Task>>
    val first: LiveData<Task>
    fun writeStartTime(game: Task)

    fun writeEndTime(game: Task)

    fun getAllGameProccesBiCabin(cabinId: Int): LiveData<List<Task>>?

    fun getLastGame(cabinId: Int):Task
    fun getLastGameLive(cabinId: Int):LiveData<Task>

    fun deleteAll()
    fun setCash(dailyCount: DailyCount)
    fun updateCash(sTimeOfDay: DateTime, endTimeOfDay: DateTime, sum: Double)
    fun getCash() : DataSource.Factory<Int, DailyCount>
    fun getGameDetails(cabinId: Int): DataSource.Factory<Int, Task>
    fun getLastCash(): DailyCount?

}