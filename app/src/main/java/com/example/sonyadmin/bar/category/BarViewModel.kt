package com.example.sonyadmin.bar.category

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sonyadmin.util.Event
import com.google.firebase.firestore.FirebaseFirestore


class BarViewModel : ViewModel() {
    var db = FirebaseFirestore.getInstance()
    private val TAG: String = BarViewModel::class.java.simpleName
    var categories: MutableLiveData<List<Category>> = MutableLiveData(arrayListOf())

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarMessage: LiveData<Event<String>> = _snackbarText

    private val _openCategoryEvent = MutableLiveData<Event<Category>>()
    val openCategoryEvent: LiveData<Event<Category>> = _openCategoryEvent

    init {
        _dataLoading.value = true
        db.collection("category")
            .get()
            .addOnSuccessListener { result ->
                var cat: List<Category> = result.map {
                    Category(it.id)
                }
                categories.postValue(cat)
                _dataLoading.value = false

            }
            .addOnFailureListener { exception ->
                _snackbarText.value = Event(exception.localizedMessage)
                _dataLoading.value = false
            }

    }

    fun openCategory(category: Category) {
        _openCategoryEvent.value = Event(category)
    }
}