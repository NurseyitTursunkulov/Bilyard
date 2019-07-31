package com.example.sonyadmin.bar.category

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sonyadmin.data.Repository.CategoryRepository
import com.example.sonyadmin.util.Event
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class BarViewModel(val categoryRepository: CategoryRepository) : ViewModel() {
    var db = FirebaseFirestore.getInstance()
    private val TAG: String = BarViewModel::class.java.simpleName
    var categories: MutableLiveData<List<Category>> = MutableLiveData(arrayListOf())

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarMessage: LiveData<Event<String>> = _snackbarText

    private val _dialog = MutableLiveData<Event<Boolean>>()
    val dialog: LiveData<Event<Boolean>> = _dialog

    private val _openCategoryEvent = MutableLiveData<Event<Category>>()
    val openCategoryEvent: LiveData<Event<Category>> = _openCategoryEvent

    init {
        getCategories()

    }

    private fun getCategories() {
        _dataLoading.value = true
        categoryRepository.getCategories(onSuccess = { result ->
            var cat: List<Category> = result.map {
                Category(it.id)
            }
            categories.postValue(cat)
            _dataLoading.value = false
        }, onFailure = { exception ->
            _snackbarText.value = Event(exception.localizedMessage)
            _dataLoading.value = false
        })
    }

    fun openCategory(category: Category) {
        _openCategoryEvent.value = Event(category)
    }

    fun addCategoryEvent() {
        _dialog.postValue(Event(true))
    }

    fun addCategory(categoryName: String) {
        categoryRepository.addCategory(categoryName)
        getCategories()
    }
}