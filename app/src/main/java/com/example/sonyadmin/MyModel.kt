package com.example.sonyadmin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MyModel : ViewModel() {
    var list :List<Game> = createList()
     fun endGame(game: Game, position: Int) {
          Log.d("Main","complete game " + game.name + " $position $game.id ")
         viewModelScope.launch {
             try {
             } catch (error: Error) {

             } finally {

             }
         }
     }

     fun startGame(game: Game, position: Int) {
         viewModelScope.launch {
             try {
             } catch (error: Error) {

             } finally {

             }
         }
          Log.d("Main","open task $game")
         list[position]
     }



    private fun createList(): List<Game> {
        var list : ArrayList<Game> = ArrayList()
        for (x in 1..10) {
            list.add(Game(id = x).apply {
                startTime.value="1$x:23"
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