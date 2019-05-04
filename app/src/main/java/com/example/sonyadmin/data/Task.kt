package com.example.sonyadmin.data

import androidx.lifecycle.MutableLiveData
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import org.joda.time.DateTime
@Entity
data class Task(@PrimaryKey(autoGenerate = true) var id: Int? = null,
                var cabinId:Int =0,
                var startTime: MutableLiveData<DateTime> = MutableLiveData(),
                var endTime: MutableLiveData<DateTime>? = MutableLiveData(),
                var summ: MutableLiveData<Double>? = MutableLiveData(),
                var isPlaying:MutableLiveData<Boolean> = MutableLiveData(false)) {
    var idForTitle = cabinId.toString() + "я кабинка"

}

class Converters {
    @TypeConverter
    fun fromLiveToString(data: MutableLiveData<String>?): String? {
        return data?.value
    }

    @TypeConverter
    fun fromStringToLive(date: String?): MutableLiveData<String>? {
        return MutableLiveData<String>().apply{value = date}
    }

    @TypeConverter
    fun fromLiveToDouble(data: MutableLiveData<Double>?): Double? {
        return data?.value
    }

    @TypeConverter
    fun fromDoubleToLive(date: Double?): MutableLiveData<Double>? {
        return MutableLiveData<Double>().apply { postValue(date) }
    }

    @TypeConverter
    fun fromTimestamp(longt: Long?):MutableLiveData< DateTime>? {
        return MutableLiveData<DateTime>().apply { longt?.let { postValue(DateTime(it)) } }
    }

    @TypeConverter
    fun dateToTimestamp(date: MutableLiveData<DateTime>?): Long? {
        return date?.value?.millis
    }
    @TypeConverter
    fun fromLiveToBool(data: MutableLiveData<Boolean>?): Boolean? {
        return data?.value
    }

    @TypeConverter
    fun fromBoolToLive(date: Boolean?): MutableLiveData<Boolean>? {
        return MutableLiveData<Boolean>().apply { postValue(date) }
    }

}