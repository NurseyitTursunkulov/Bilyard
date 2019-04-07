package com.example.sonyadmin.gameList

import androidx.lifecycle.MutableLiveData

data class Game(var name : String = "name", var id : Int = 0,
                var startTime : MutableLiveData<String> = MutableLiveData(),
                var endTime : MutableLiveData<String> = MutableLiveData(),
                var summ : MutableLiveData<String> = MutableLiveData()) {
    var idForTitle = id.toString() + "я кабинка"


}
