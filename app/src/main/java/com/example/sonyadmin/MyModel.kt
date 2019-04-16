package com.example.sonyadmin

import android .util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.joda.time.DateTime

class MyModel : ViewModel() {
     var list = listOf<MutableLiveData<Task>>(
          MutableLiveData<Task>(Task("Nuts", 0)),
          MutableLiveData<Task>(Task("Nuts", 1)),
          MutableLiveData<Task>(Task("Nuts", 2)),
          MutableLiveData<Task>(Task("Nuts", 3)),
          MutableLiveData<Task>(Task("Nuts", 4)),
          MutableLiveData<Task>(Task("Nuts", 5)),
          MutableLiveData<Task>(Task("Nuts", 6)),
          MutableLiveData<Task>(Task("Nuts", 7)),
          MutableLiveData<Task>(Task("Nuts", 8)),
          MutableLiveData<Task>(Task("Nuts", 9))

          )
     fun completeTask(task: Task) {
          Log.d("Main","complete task " + task.id + " ")
         items.value?.get(task.id)?.value?.apply {
             startTime?.value = DateTime.now()
             isPlaying.value = true
             Log.d("Main","inside complete ${this.id}")
         }
     }

     fun openTask(task: Task) {
          Log.d("Main","open task ${task.id} isplaing = ${task.isPlaying.value}")
          items.value?.get(task.id)?.value?.apply {
              startTime?.value = DateTime.now()
              isPlaying.value = false
              Log.d("Main","inside open ${this.id}")
          }
     }

     val items = MutableLiveData<List<MutableLiveData<Task>>>()



     init {
          items.value=list

        Log.d("Bag","init")
     }
}