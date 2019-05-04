package com.example.sonyadmin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(Task::class),version = 8)
@TypeConverters(Converters::class)
abstract class GameProcessDataBase : RoomDatabase(){
    abstract fun gameProcesDao():Dao

    companion object {
        private var INSTANCE: GameProcessDataBase? = null

        private val lock = Any()

        fun getInstance(context: Context): GameProcessDataBase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        GameProcessDataBase::class.java, "GameProcess.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return INSTANCE!!
            }
        }
    }
}