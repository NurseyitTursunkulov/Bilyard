package com.example.sonyadmin.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime

@Entity
data class DailyCount(
    @PrimaryKey
    var date: DateTime,
    var userName: String,
    var day: Int,
    var summ: Double = 0.0
)