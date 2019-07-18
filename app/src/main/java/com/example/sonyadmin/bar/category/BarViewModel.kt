package com.example.sonyadmin.bar.category

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore


class BarViewModel : ViewModel() {
    var db = FirebaseFirestore.getInstance()
    private val TAG: String = BarViewModel::class.java.simpleName
    var categories: MutableLiveData<List<Category>> = MutableLiveData(arrayListOf())

    init {
        db.collection("category")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                var cat: List<Category> = result.map {
                    Category(it.id)
                }
                categories.postValue(cat)

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        db.collection("category").document("drinks").get()
            .addOnSuccessListener {
                Log.d(TAG, it.data?.get("fanta")?.toString())
            }
            .addOnFailureListener {
                Log.w(TAG, "Error getting snap.", it)
            }
    }
}