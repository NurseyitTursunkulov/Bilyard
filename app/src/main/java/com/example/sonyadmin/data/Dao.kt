package com.example.sonyadmin.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.joda.time.DateTime

@Dao
interface Dao {

    @Insert
    fun insertStartGameProcess(gameProcess: Task)

    @Query("update Task set endTime = :endTime, summ = :summ,isPlaying = :isPlaying  where id =:id ")
    fun insertEndGameProcees( endTime: MutableLiveData<DateTime>,summ: MutableLiveData<Double>?,
                              isPlaying:MutableLiveData<Boolean>,id: Int)

    @Query("select * from Task ")
    fun getAllGameProccesBiCabin() :LiveData<List<Task>>?

    @Query("select * from Task where cabinId = :cabinId and startTime = (select MAX(startTime)  from Task where cabinId = :cabinId)")
    fun getLastGameProcessById(cabinId: Int) : Task?

//    @Query("select * from task where id = :id")
//    fun getById(id:Int) :  LiveData<Task>

    @Query("delete  from task")
    fun deleteAll()

    @Query("select * from Task where cabinId = :cabinId order by startTime desc")
    fun getAllGameProccesBiCabin(cabinId: Int): DataSource.Factory<Int, Task>
}