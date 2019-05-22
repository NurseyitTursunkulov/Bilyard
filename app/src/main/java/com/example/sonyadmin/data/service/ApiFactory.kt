package io.navendra.retrofitkotlindeferred.service



object ApiFactory{
    const val JSON_PLACEHOLDER_BASE_URL = "http://jsonplaceholder.typicode.com/"
    val USER_SERVICE : UserService = RetrofitFactory.retrofit(JSON_PLACEHOLDER_BASE_URL)
                                                .create(UserService::class.java)


}