package com.example.sonyadmin.data.service

import com.example.sonyadmin.data.Result

interface Api {
    suspend fun off(led:String): Result<ResponseType>
    suspend fun onn(led:String):Result<ResponseType>
}