package com.example.sonyadmin.data.Repository

import com.example.sonyadmin.bar.product.Product
import com.google.firebase.firestore.DocumentSnapshot

interface ProductRepository {
    fun addProduct(product: Product)

    fun getProducts(
        product: String,
        onSuccess:
            (DocumentSnapshot) -> Unit,
        onFailure:
            (Exception) -> Unit
    )
}