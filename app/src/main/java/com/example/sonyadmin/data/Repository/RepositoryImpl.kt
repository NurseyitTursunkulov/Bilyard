package com.example.sonyadmin.data.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.example.sonyadmin.bar.product.Product
import com.example.sonyadmin.data.DailyCount
import com.example.sonyadmin.data.Dao
import com.example.sonyadmin.data.Task
import com.google.firebase.firestore.FirebaseFirestore
import org.joda.time.DateTime

class RepositoryImpl(var dao: Dao) : Repository {
    private val TAG = RepositoryImpl::class.java.simpleName
    var db = FirebaseFirestore.getInstance()
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


        db.collection("games").document(game.idForTitle)
            .set(game)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
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
                db.collection("games").document(game.idForTitle)
                    .set(game)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
            }
        }
    }

    override fun getAllGameProccesBiCabin(cabinId: Int): LiveData<List<Task>>? {
        return dao.getAllGameProccesBiCabin()
    }

    override fun addBarProduct(listOfBars: ArrayList<Product>, id: Int) {
        val game = dao.getGameById(id)
        listOfBars.addAll(game.listOfBars)
        dao.addBar(listOfBars, id)
        game.listOfBars = listOfBars
        db.collection("games").document(game.idForTitle)
            .set(game)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

}