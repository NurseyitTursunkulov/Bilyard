package com.example.sonyadmin.data.service

import com.example.sonyadmin.data.Result
import retrofit2.Response

interface Api {
    suspend fun off(led:String): Result<PlaceholderPosts>
    suspend fun onn(led:String):Result<PlaceholderPosts>
}