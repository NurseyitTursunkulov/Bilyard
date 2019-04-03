package com.example.sonyadmin

import androidx.constraintlayout.widget.Placeholder
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

//ApiFactory to create jsonPlaceHolder Api
object Apifactory{
    fun retrofit() : Retrofit = Retrofit.Builder()
        .client(OkHttpClient().newBuilder().build())
        .baseUrl("https://my-json-server.typicode.com/NurseyitTursunkulov/MyServer/db/")
        .addConverterFactory(GsonConverterFactory.create(
            GsonBuilder()
            .setLenient()
            .create()))
//        .baseUrl("https://jsonplaceholder.typicode.com")
//        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val placeHolderApi : PlaceholderApi = retrofit().create(PlaceholderApi::class.java)
}

//Data Model for Post
// Data model for user
data class PlaceholderUsers(
    val id:Int,
    val name: String,
    val username: String,
    val email: String,
    val address: PlaceholderAddress,
    val phone: String,
    val website: String,
    val company: PlaceholderCompany
)

/**
 * Helper data classes for PlaceholderUsers
 */
data class PlaceholderAddress(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: PlaceholderGeo
)

data class PlaceholderGeo(
    val lat: String,
    val lng: String
)

data class PlaceholderCompany(
    val name: String,
    val catchPhrase: String,
    val bs: String
)


//A retrofit Network Interface for the Api
interface PlaceholderApi{
    @GET("/")
    fun getPosts() : Deferred<String>


    @GET("/users")
    fun getUsers() : Deferred<Response<List<PlaceholderUsers>>>
}

