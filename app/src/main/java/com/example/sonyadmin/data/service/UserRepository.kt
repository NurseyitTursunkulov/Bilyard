package com.example.sonyadmin.data.service

import io.navendra.retrofitkotlindeferred.service.UserService
import retrofit2.Response

class UserRepository(private val service: UserService) {

    suspend fun off(led: String): Response<ResponseType> {
        var h = service.off(led).await()

        h.body()
        return h
    }

    suspend fun onn(led: String): Response<ResponseType> {
        var h = service.onn(led).await()

        h.body()
        return h
    }


}