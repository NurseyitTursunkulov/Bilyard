package com.example.sonyadmin.data.Repository

import com.example.sonyadmin.bar.product.Product
import com.google.firebase.firestore.QuerySnapshot

interface CategoryRepository {
    fun getCategories(
        onSuccess:
            (QuerySnapshot) -> Unit,
        onFailure:
            (Exception) -> Unit
    )

    fun addCategory(categoryName: String)
}