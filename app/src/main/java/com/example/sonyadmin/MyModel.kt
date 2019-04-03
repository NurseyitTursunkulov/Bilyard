package com.example.sonyadmin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.URL

class MyModel : ViewModel() {
    fun refreshTitle() {
        launchDataLoad {
            val apiResponse = URL("https://my-json-server.typicode.com/NurseyitTursunkulov/MyServer/db").readText()
            Log.d("Main", "after delay $apiResponse")
        }
    }

    val spinner = MutableLiveData<Boolean>()
    val _snackBar = MutableLiveData<String>()

    class TitleRefreshError(cause: Throwable) : Throwable(cause.message, cause)

    private fun launchDataLoad(block: suspend () -> Unit): Job {


        return viewModelScope.launch(Dispatchers.IO) {
            try {
                spinner.postValue(true)

                block()
            } catch (error: TitleRefreshError) {
                _snackBar.postValue(error.message)
            } finally {
                spinner.postValue(false)
            }
        }
    }
}