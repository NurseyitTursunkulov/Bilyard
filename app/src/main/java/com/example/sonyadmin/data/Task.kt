package com.example.sonyadmin.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import org.joda.time.DateTime

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var cabinId: Int = 0,
    var startTime: DateTime,
    var endTime: DateTime? = null,
    var summ: Double = 0.0,
    var userName: String,
    var isPlaying: Boolean = false
) {
    var idForTitle = cabinId.toString() + "я кабинка"

}

class Converters {


    @TypeConverter
    fun fromTime(value: Long?): DateTime? {
        return value?.let { DateTime(it) }
    }

    @TypeConverter
    fun dateToTime(date: DateTime?): Long? {
        return date?.millis
    }

}