package com.example.sonyadmin.data.service

import android.util.Log
import io.navendra.retrofitkotlindeferred.service.UserService
import retrofit2.Response

class UserRepository(private val service: UserService) {

     suspend fun search() : Response<List<PlaceholderPosts>> {
        var h = service.off().await()

         h.body()
         return h
     }

    suspend fun onn() : Response<List<PlaceholderPosts>> {
        var h = service.onn().await()

        h.body()
        return h
    }


}