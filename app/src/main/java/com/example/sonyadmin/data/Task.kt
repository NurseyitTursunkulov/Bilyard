package com.example.sonyadmin.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.sonyadmin.bar.product.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.joda.time.DateTime


@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var cabinId: Int = 0,
    var startTime: DateTime,
    var endTime: DateTime? = null,
    var summ: Double = 0.0,
    var userName: String,
    var listOfBars: ArrayList<Product>,
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

    @TypeConverter
    fun fromCountryLangList(productList: ArrayList<Product>?): String? {
        if (productList == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Product>>() {

        }.getType()
        return gson.toJson(productList, type)
    }

    @TypeConverter
    fun toCountryLangList(ProductListString: String?): ArrayList<Product>? {
        if (ProductListString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Product>>() {

        }.getType()
        return gson.fromJson<ArrayList<Product>>(ProductListString, type)
    }

}