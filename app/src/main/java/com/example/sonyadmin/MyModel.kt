package com.example.sonyadmin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyModel : ViewModel() {
     fun completeTask(task: Task, checked: Boolean) {
          Log.d("Main","complete task " + task.name + checked)
     }

     fun openTask(id: Any) {
          Log.d("Main","open task $id")
     }

     val items = MutableLiveData<List<Task>>().apply { value = emptyList() }

}