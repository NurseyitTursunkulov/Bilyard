package com.example.sonyadmin.data

import com.example.sonyadmin.gameList.Game

interface Repository {
    suspend fun writeStartTime( game: Game)

    suspend fun writeEndTime(game: Game)

    suspend fun getAllGameProccesBiCabin(cabinId :Int) : List<GameProcess>

    fun getLastGameProcessById(cabinId: Int) : GameProcess
}