package com.example.sonyadmin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.selects.select

@Dao
interface GameProcessDAO{
    @Insert
    fun insertStartGameProcess(gameProcess: GameProcess)

    @Update
    fun insertEndGameProcees(gameProcess: GameProcess)

    @Query("select * from gameprocess where cabinId = :cabinId ")
    fun getAllGameProccesBiCabin(cabinId :Int) : List<GameProcess>

    @Query("select * from gameprocess where cabinId = :cabinId and startTime = (select MAX(startTime)  from gameprocess where cabinId = :cabinId)")
    fun getLastGameProcessById(cabinId: Int) : GameProcess

    @Query("select * from gameprocess where id = :id")
    fun getById(id:Int) : GameProcess

    @Query("delete  from gameProcess")
    fun deleteAll()




}