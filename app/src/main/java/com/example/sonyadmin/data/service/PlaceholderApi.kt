package io.navendra.retrofitkotlindeferred.service


import com.example.sonyadmin.data.service.PlaceholderPosts
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PlaceholderApi{

//    @GET("/off/{led}")
//    fun off(@Path("led")led: Int) : Deferred<Response<List<PlaceholderPosts>>>
//    @GET("/onn/{led}")
//    fun onn(@Path("led")led: Int) : Deferred<Response<List<PlaceholderPosts>>>
    @GET("/posts")
    fun off() : Deferred<Response<List<PlaceholderPosts>>>
    @GET("/posts")
    fun onn() : Deferred<Response<List<PlaceholderPosts>>>

}