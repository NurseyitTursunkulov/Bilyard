package com.example.sonyadmin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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
     fun completeTask(task: Task, checked: Boolean) {
          Log.d("Main","complete task " + task.name + " $checked $task.id ")
     }

     fun openTask(id: Any) {
          Log.d("Main","open task $id")
          items.value?.get(4)?.value=Task(id = 55)
     }

     val items = MutableLiveData<List<MutableLiveData<Task>>>()



     init {
          items.value=list


     }
}