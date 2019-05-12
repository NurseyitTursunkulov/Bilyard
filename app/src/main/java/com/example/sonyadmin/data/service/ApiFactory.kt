package io.navendra.retrofitkotlindeferred.service



object ApiFactory{
    const val JSON_PLACEHOLDER_BASE_URL = "http://jsonplaceholder.typicode.com/"
    val placeholderApi : PlaceholderApi = RetrofitFactory.retrofit(JSON_PLACEHOLDER_BASE_URL)
                                                .create(PlaceholderApi::class.java)


}