package com.example.sonyadmin.data.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.example.sonyadmin.bar.product.Product
import com.example.sonyadmin.data.DailyCount
import com.example.sonyadmin.data.Dao
import com.example.sonyadmin.data.Task
import org.joda.time.DateTime

class RepositoryImpl(var dao: Dao) : Repository {
    override var list: ArrayList<LiveData<Task>>
        get() = getAllgames()
        set(value) {}

    private fun getAllgames(): ArrayList<LiveData<Task>> {
        var list = ArrayList<LiveData<Task>>()
        for (x in 0..10) {
            list.add(dao.getOneGameLive(x))
        }
        return list
    }

    override val first: LiveData<Task>
        get() = dao.getOneGameLive(3)

    override fun getLastGameLive(cabinId: Int): LiveData<Task> {
        return dao.getOneGameLive(cabinId)
    }

    override fun getLastCash(): DailyCount? {
        return dao.getLastCash()
    }

    override fun getCash(): DataSource.Factory<Int, DailyCount> {
        return dao.getCash()
    }

    override fun setCash(dailyCount: DailyCount) {
        dao.setCash(dailyCount)
    }

    override fun updateCash(sTimeOfDay: DateTime, endTimeOfDay: DateTime, day: Int, sum: Double) {
        dao.updateCash(sTimeOfDay, endTimeOfDay, day, sum)
    }


    override fun getGameDetails(cabinId: Int): DataSource.Factory<Int, Task> {
        return dao.getAllGameProccesBiCabin(cabinId)
    }

    override fun deleteAll() {
        dao.deleteAll()
    }

    override fun getLastGame(cabinId: Int): Task {
        var data = dao.getLastGameProcessById(cabinId)
//        Log.d("DataBase", "getLast = ${data?.cabinId} ${data?.startTime}")
        return data


    }

    override fun writeStartTime(game: Task) {
        dao.insertStartGameProcess(game)
    }

    override fun writeEndTime(game: Task) {
//        Log.d("DataBase", "writeEndtime = ${game?.startTime.value} ${game?.endTime?.value}")
        if (game.endTime != null) {
            if (game.id != null)
                dao.insertEndGameProcees(game.endTime!!, game.summ, game.isPlaying, game.id!!)
        }

    }

    override fun getAllGameProccesBiCabin(cabinId: Int): LiveData<List<Task>>? {
        return dao.getAllGameProccesBiCabin()
    }

    override fun addBarProduct(listOfBars: ArrayList<Product>, id: Int) {
        val prevBar = dao.getGameById(id).listOfBars
        listOfBars.addAll(prevBar)
        Log.d("Repo"," listOfProducts $prevBar" )
        dao.addBar(listOfBars, id)
    }

}