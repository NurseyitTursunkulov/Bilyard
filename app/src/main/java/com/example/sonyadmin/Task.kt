package com.example.sonyadmin

import androidx.lifecycle.MutableLiveData
import org.joda.time.DateTime

data class Task(var name : String = "name", var id : Int = 0,
                var startTime : MutableLiveData<String> = MutableLiveData(),
                var endTime : MutableLiveData<String> = MutableLiveData(),
                var summ : MutableLiveData<String> = MutableLiveData(),
                var isPlaying:MutableLiveData<Boolean> = MutableLiveData(false)) {
    var idForTitle = id.toString() + "я кабинка"

}