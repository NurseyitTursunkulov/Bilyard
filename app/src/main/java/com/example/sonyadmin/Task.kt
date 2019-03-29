package com.example.sonyadmin

data class Task(var name : String = "name", var id : Int = 0) {
    var idForTitle = id.toString() + "я кабинка"
}
