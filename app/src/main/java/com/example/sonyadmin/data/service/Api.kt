package com.example.sonyadmin.data.service
import com.example.sonyadmin.data.Result

interface Api {
    suspend fun makeRequest(cabinId : Int): Result<Boolean>
}