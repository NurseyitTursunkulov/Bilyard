package com.example.sonyadmin.data.Repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.example.sonyadmin.bar.product.Product
import com.example.sonyadmin.data.DailyCount
import com.example.sonyadmin.data.Task
import org.joda.time.DateTime


interface Repository : FirebaseRepository{
    var list: ArrayList<LiveData<Task>>
    val first: LiveData<Task>


    fun getAllGameProccesBiCabin(cabinId: Int): LiveData<List<Task>>?

    fun getLastGame(cabinId: Int): Task
    fun getLastGameLive(cabinId: Int): LiveData<Task>

    fun deleteAll()


    fun getCash(): DataSource.Factory<Int, DailyCount>
    fun getGameDetails(cabinId: Int): DataSource.Factory<Int, Task>
    fun getLastCash(): DailyCount?

}