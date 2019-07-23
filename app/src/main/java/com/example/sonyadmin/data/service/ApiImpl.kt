package com.example.sonyadmin.data.service

import com.example.sonyadmin.data.Result
import io.navendra.retrofitkotlindeferred.service.UserService

class ApiImpl(val userService: UserService) : Api {

    override suspend fun off(led: String): Result<ResponseType> {
        try {
            val call = userService.off(led).await()
            if (call.isSuccessful) {
                return Result.Success(call.body()!!)
            } else {
                return Result.Error(Exception("no connection"))
            }
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    override suspend fun onn(led: String): Result<ResponseType> {
        try {
            val call = userService.onn(led).await()
            if (call.isSuccessful) {
                return Result.Success(call.body()!!)
            } else {
                return Result.Error(Exception("no connection"))
            }
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

}