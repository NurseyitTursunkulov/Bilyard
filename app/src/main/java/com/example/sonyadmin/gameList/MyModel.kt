package com.example.sonyadmin.gameList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sonyadmin.data.Repository
import kotlinx.coroutines.launch
import java.util.*

class MyModel(val repository: Repository) : ViewModel() {
    var list: List<Game> = createList()
    fun endGame(game: Game, position: Int) {
        viewModelScope.launch {
                repository.writeEndTime(game)
                repository.getAllGameProccesBiCabin(game.id).forEach {
                    Log.d("DataBase", "get all${it.startTime}")
                }
        }
    }

    fun startGame(game: Game, position: Int) {
        viewModelScope.launch {
                repository.writeStartTime(game)
        }
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