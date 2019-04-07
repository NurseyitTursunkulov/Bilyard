package com.example.sonyadmin.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.*

@Entity
data class GameProcess(@PrimaryKey(autoGenerate = true) var id : Int? = null,
                       var startTime : Date = Date(),
                       var endTime : Date?,
                       var sum : Double?,
                       var cabinId : Int,
                       var finished :Boolean = false){

}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}