package com.example.sonyadmin.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.joda.time.DateTime

@Dao
interface Dao {

    @Query("select count(*) from Task")
    fun count(): Int

    @Insert
    fun insertStartGameProcess(gameProcess: Task)

    @Query("update Task set endTime = :endTime, summ = :summ,isPlaying = :isPlaying  where id =:id ")
    fun insertEndGameProcees(
        endTime: DateTime, summ: Double,
        isPlaying: Boolean, id: Int
    )

    @Query("select * from Task ")
    fun getAllGameProccesBiCabin(): LiveData<List<Task>>?

    @Query("select * from Task where cabinId = :cabinId ORDER BY id DESC limit 1")
    fun getOneGameLive(cabinId: Int): LiveData<Task>

    @Query("select * from Task limit 1")
    fun getOneGame(): Task

    @Insert
    fun setCash(dailyCount: DailyCount)

    @Query("update DailyCount set summ = summ + :sum where day ==:day and date between  :sTimeOfDay and :endTimeOfDay")
    fun updateCash(sTimeOfDay: DateTime, endTimeOfDay: DateTime, day: Int, sum: Double)

    @Query("select * from DailyCount order by date desc")
    fun getCash(): DataSource.Factory<Int, DailyCount>

    @Query("select  * from DailyCount ORDER BY date DESC LIMIT 1")
    fun getLastCash(): DailyCount?

    @Query("select * from Task where cabinId = :cabinId and startTime = (select MAX(startTime)  from Task where cabinId = :cabinId)")
    fun getLastGameProcessById(cabinId: Int): Task

//    @Query("select * from task where id = :id")
//    fun getById(id:Int) :  LiveData<Task>

    @Query("delete  from task")
    fun deleteAll()

    @Query("select * from Task where cabinId = :cabinId order by startTime desc")
    fun getAllGameProccesBiCabin(cabinId: Int): DataSource.Factory<Int, Task>
}