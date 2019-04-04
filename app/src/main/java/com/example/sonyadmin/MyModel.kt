package com.example.sonyadmin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyModel : ViewModel() {
     fun completeTask(task: Task, checked: Boolean) {
          Log.d("Main","complete task " + task.name + " $checked $task.id ")
     }

     fun openTask(id: Any) {
          Log.d("Main","open task $id")
     }

     val items = MutableLiveData<List<Task>>().apply {
          value = listOf<Task>(
               Task("Nuts", 0), Task(id = 1), Task("Nuts", 2), Task(id = 3), Task(id = 4), Task(
                    "Nuts",
                    5
               ), Task("wrt", 6), Task("juuuh", 7), Task(id = 8)
          )
     }

}