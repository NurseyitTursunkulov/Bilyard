package io.navendra.retrofitkotlindeferred.service


import com.example.sonyadmin.data.service.ResponseType
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService{

//    @GET("/off/{led}")
//    fun off(@Path("led")led: Int) : Deferred<Response<List<ResponseType>>>
//    @GET("/onn/{led}")
//    fun onn(@Path("led")led: Int) : Deferred<Response<List<ResponseType>>>
    @GET("/posts/{led}")
    fun off(@Path("led") led: String) : Deferred<Response<ResponseType>>
    @GET("/posts/{led}")
    fun onn(@Path("led") led: String) : Deferred<Response<ResponseType>>

}