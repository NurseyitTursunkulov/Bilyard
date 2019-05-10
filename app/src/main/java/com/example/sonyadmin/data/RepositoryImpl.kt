package com.example.sonyadmin.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import org.joda.time.DateTime

class RepositoryImpl(var dao: com.example.sonyadmin.data.Dao) : Repository {
    override fun getCash(): DataSource.Factory<Int, DailyCount> {
      return  dao.getCash()
    }

    override fun setCash(dailyCount: DailyCount) {
        dao.setCash(dailyCount)
    }

    override fun updateCash(sTimeOfDay: DateTime, endTimeOfDay: DateTime, sum: Double) {
        dao.updateCash(sTimeOfDay,endTimeOfDay,sum)
    }


    override fun getGameDetails(cabinId: Int): DataSource.Factory<Int, Task> {
        return dao.getAllGameProccesBiCabin(cabinId)
    }

    override fun deleteAll() {
        dao.deleteAll()
    }

    override fun getLastGame(cabinId: Int): MutableLiveData<Task>? {
        var data = dao.getLastGameProcessById(cabinId)
        Log.d("DataBase", "getLast = ${data?.cabinId} ${data?.startTime}")
        return if (data != null)
            MutableLiveData<Task>(data)
        else null


    }

    override fun writeStartTime(game: Task) {
        dao.insertStartGameProcess(game)
    }

    override fun writeEndTime(game: Task) {
        Log.d("DataBase", "writeEndtime = ${game?.startTime.value} ${game?.endTime?.value}")
        if (game.endTime != null) {
            if (game.id != null)
                dao.insertEndGameProcees(game.endTime!!, game.summ, game.isPlaying, game.id!!)
        }

    }

    override fun getAllGameProccesBiCabin(cabinId: Int): LiveData<List<Task>>? {
        return dao.getAllGameProccesBiCabin()
    }

}