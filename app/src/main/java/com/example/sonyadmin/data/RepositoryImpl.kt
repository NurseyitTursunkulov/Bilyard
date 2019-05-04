package com.example.sonyadmin.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

class RepositoryImpl(var dao: com.example.sonyadmin.data.Dao) : Repository {
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
        dao.insertEndGameProcees(game)
    }

    override fun getAllGameProccesBiCabin(cabinId: Int): LiveData<List<Task>>? {
        return dao.getAllGameProccesBiCabin()
    }

}