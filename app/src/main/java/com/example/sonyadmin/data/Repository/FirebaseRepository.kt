package com.example.sonyadmin.data.Repository

import com.example.sonyadmin.bar.product.Product
import com.example.sonyadmin.data.DailyCount
import com.example.sonyadmin.data.Task
import org.joda.time.DateTime

interface FirebaseRepository {
    fun addBarProduct(listOfBars:ArrayList<Product>, task: Task)
    fun setCash(dailyCount: DailyCount)
    fun updateCash(sTimeOfDay: DateTime, endTimeOfDay: DateTime, day: Int, sum: Double)
    fun writeStartTime(game: Task)

    fun writeEndTime(game: Task)
}