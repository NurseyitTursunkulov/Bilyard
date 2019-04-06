package com.example.sonyadmin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyModel : ViewModel() {
     fun completeTask(game: Game, checked: Boolean) {
          Log.d("Main","complete game " + game.name + " $checked $game.id ")
     }

     fun openTask(id: Any, position: Int) {
          Log.d("Main","open task $id")
         list[position]
     }

     var list :List<Game> = createList()

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