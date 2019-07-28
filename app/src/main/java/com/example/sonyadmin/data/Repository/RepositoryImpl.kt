package com.example.sonyadmin.data.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.example.sonyadmin.bar.product.Product
import com.example.sonyadmin.data.DailyCount
import com.example.sonyadmin.data.Dao
import com.example.sonyadmin.data.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import org.joda.time.DateTime

class RepositoryImpl(var dao: Dao,val firebaseRepository: FirebaseRepository) : Repository {

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
        firebaseRepository.setCash(dailyCount)
    }

    override fun updateCash(sTimeOfDay: DateTime, endTimeOfDay: DateTime, day: Int, sum: Double) {
        dao.updateCash(sTimeOfDay, endTimeOfDay, day, sum)
        firebaseRepository.updateCash(sTimeOfDay, endTimeOfDay, day, sum)
    }


    override fun getGameDetails(cabinId: Int): DataSource.Factory<Int, Task> {
        return dao.getAllGameProccesBiCabin(cabinId)
    }

    override fun deleteAll() {
        dao.deleteAll()
    }

    override fun getLastGame(cabinId: Int): Task {
        var data = dao.getLastGameProcessById(cabinId)
        return data
    }

    override fun writeStartTime(game: Task) {
        dao.insertStartGameProcess(game)
        firebaseRepository.writeStartTime(game)
    }

    override fun writeEndTime(game: Task) {
        if (game.endTime != null) {
            if (game.id != null) {
                dao.insertEndGameProcees(
                    game.endTime!!,
                    game.summOfTheGame,
                    game.totalSumWithBar,
                    game.isPlaying,
                    game.id!!
                )
                firebaseRepository.writeEndTime(game)
            }
        }
    }

    override fun getAllGameProccesBiCabin(cabinId: Int): LiveData<List<Task>>? {
        return dao.getAllGameProccesBiCabin()
    }

    override fun addBarProduct(listOfBars: ArrayList<Product>, task: Task) {
         dao.getGameById(task.id!!).apply {
            this.listOfBars.addAll(listOfBars)
            dao.addBar(this.listOfBars, task.id!!)
        }
        firebaseRepository.addBarProduct(listOfBars,task)
    }

}