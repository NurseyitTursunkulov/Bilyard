package com.example.sonyadmin.data.Repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.example.sonyadmin.data.DailyCount
import com.example.sonyadmin.data.Task
import org.joda.time.DateTime

class FakeRepo : Repository {
    override var list: ArrayList<LiveData<Task>>
        get() = arrayListOf<LiveData<Task>>()
        set(value) {}
    override val first: LiveData<Task>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun writeStartTime(game: Task) {
    }

    override fun writeEndTime(game: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllGameProccesBiCabin(cabinId: Int): LiveData<List<Task>>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLastGame(cabinId: Int): Task {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLastGameLive(cabinId: Int): LiveData<Task> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setCash(dailyCount: DailyCount) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCash(sTimeOfDay: DateTime, endTimeOfDay: DateTime, day:Int, sum: Double) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCash(): DataSource.Factory<Int, DailyCount> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGameDetails(cabinId: Int): DataSource.Factory<Int, Task> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLastCash(): DailyCount? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}