package com.example.sonyadmin.bar.product

import com.example.sonyadmin.bar.category.Category
data class ProductDetails(var amount:Long , var price: Double ,var volume : Double = 1.0)
data class Product(var name : String , var details : ProductDetails)
data class Products(var list:List<Product>)