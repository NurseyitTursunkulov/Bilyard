package com.example.sonyadmin.data.Repository

import android.util.Log
import com.example.sonyadmin.bar.category.Category
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class CategoryRepoImpl : CategoryRepository {

    var db = FirebaseFirestore.getInstance()

    override fun getCategories(onSuccess:(QuerySnapshot) -> Unit,
        onFailure:(Exception) -> Unit)
    {
        db.collection("category")
            .get()
            .addOnSuccessListener {
                onSuccess(it)
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }

    override fun addCategory(categoryName: String) {
        db.collection("category").document(categoryName).set(Category(categoryName))
        val updates = hashMapOf<String, Any>(
            "categoryName" to FieldValue.delete()
        )
        db.collection("category").document(categoryName)
            .update(updates).addOnCompleteListener {
                Log.d("Main", "success")
            }
    }

}