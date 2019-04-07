package com.example.sonyadmin.data

import android.app.Application
import android.util.Log
import com.example.sonyadmin.gameList.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class RepositoryImpl(application: Application) : Repository {
    val db = GameProcessDataBase.getInstance(application).gameProcesDao()
    override suspend fun writeStartTime(game: Game) {
        withContext(Dispatchers.IO) {
            db.insertStartGameProcess(
                GameProcess(
                    cabinId = game.id,
                    startTime = Date(), endTime = null, sum = null
                )
            )
            Log.d("DataBase", "writeStartTime")
        }
    }

    override suspend fun writeEndTime(game: Game) {
        withContext(Dispatchers.IO) {
            var gameProcess = db.getLastGameProcessById(game.id)
            gameProcess.endTime = Date()
            gameProcess.finished = true
            db.insertEndGameProcees(gameProcess)
            db.getById(gameProcess.id!!).let {
                Log.d("DataBase", " id = ${it.id} start : ${it.startTime} end = ${it.endTime}")
            }
            Log.d("DataBase", "writeEndTime")
            db.getAllGameProccesBiCabin(game.id).forEach {
                Log.d("DataBase","for each ${it.id} cabin id = ${it.cabinId}  ${it.startTime}")
            }
        }
    }

//    override suspend fun getAllGameProccesBiCabin(cabinId: Int): List<GameProcess> {
//        var list : List<GameProcess>
//        withContext(Dispatchers.IO) {
//            list = db.getAllGameProccesBiCabin(cabinId)
//        }
//        return listOf(GameProcess(cabinId = 1,))
//    }

    override fun getLastGameProcessById(cabinId: Int): GameProcess {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}