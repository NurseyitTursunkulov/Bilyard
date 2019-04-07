package com.example.sonyadmin.gameList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sonyadmin.data.GameProcess
import com.example.sonyadmin.data.GameProcessDataBase.Companion.getInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MyModel(application: Application) : AndroidViewModel(application) {
    var list: List<Game> = createList()
    val db = getInstance(application).gameProcesDao()
    fun endGame(game: Game, position: Int) {
        Log.d("Main", "complete game " + game.name + " $position $game.id ")
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                   var gameProcess = db.getLastGameProcessById(game.id)
                    gameProcess.endTime = Date()
                    gameProcess.finished = true
                    db.insertEndGameProcees(gameProcess)
                    db.getById(gameProcess.id!!).let {
                        Log.d("DataBase", " id = ${it.id } start : ${it.startTime} end = ${it.endTime}")
                    }

                }
            } catch (error: Error) {
                Log.d("DataBase", "error endgame $error")
            } finally {

            }
        }
    }

    fun startGame(game: Game, position: Int) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    db.insertStartGameProcess(GameProcess(cabinId = game.id,
                        startTime = Date(), endTime = null,sum = null))
                }
            } catch (error: Error) {
                Log.d("DataBase", "error startGAme $error")
            } finally {

            }
        }
        Log.d("Main", "open task $game")
        list[position]
    }


    private fun createList(): List<Game> {
        var list: ArrayList<Game> = ArrayList()
        for (x in 1..10) {
            list.add(Game(id = x).apply {
                startTime.value = "1$x:23"
                endTime.value = "2$x:12"
                summ.value = "$x сом"
            })
        }
        return list
    }

    val items = MutableLiveData<List<Game>>().apply {
        value = list
    }

}