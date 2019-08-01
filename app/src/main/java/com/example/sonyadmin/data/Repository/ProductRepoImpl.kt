package com.example.sonyadmin.data.Repository

import android.util.Log
import com.example.sonyadmin.bar.product.Product
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ProductRepoImpl : ProductRepository {

    var db = FirebaseFirestore.getInstance()

    override fun addProduct(product: Product) {

        val details = hashMapOf(
            "price" to product.details.price,
            "amount" to product.details.amount
        )
        val prod = hashMapOf(product.name to details

            )
        db.collection("category").document("наркотики")
            .update(prod as Map<String, Any>).addOnSuccessListener {
                Log.d("Main", "success")
            }
            .addOnFailureListener {
                Log.d("Main", "failure")
            }
    }

    override fun getProducts(product: String, onSuccess: (DocumentSnapshot) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("category").document(product).get()
            .addOnSuccessListener { documentSnapshot ->
                onSuccess(documentSnapshot)
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }
}