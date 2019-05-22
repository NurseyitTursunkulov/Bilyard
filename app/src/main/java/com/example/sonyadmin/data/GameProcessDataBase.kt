package com.example.sonyadmin.data

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.sonyadmin.gameList.ScopeProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.koin.core.KoinComponent
import org.koin.core.inject

@Database(entities = [Task::class, DailyCount::class],version = 8)
@TypeConverters(Converters::class)
abstract class GameProcessDataBase : RoomDatabase() {
    abstract fun gameProcesDao(): Dao


    private class PopulateDbAsync internal constructor(db: GameProcessDataBase) : AsyncTask<Void, Void, Void>() {

        private val mDao: Dao

        init {
            mDao = db.gameProcesDao()
        }

        override fun doInBackground(vararg params: Void): Void? {
            // Start the app with a clean database every time.
            if (mDao.count()==0){
                for (x in 0..10){
                    mDao.insertStartGameProcess(Task(cabinId = x,startTime = DateTime.now(),
                        endTime = DateTime.now(),summ = 0.0, isPlaying = false))
                    Log.d("Database","new ")
                }
            }
            return null
        }
    }

    companion object {
        private var INSTANCE: GameProcessDataBase? = null

        private val lock = Any()

        fun getInstance(context: Context): GameProcessDataBase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        GameProcessDataBase::class.java, "GameProcess.db"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(sRoomDatabaseCallback)
                        .build()
                }
                return INSTANCE!!
            }
        }

        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                PopulateDbAsync(INSTANCE!!).execute()
            }
        }
    }


}