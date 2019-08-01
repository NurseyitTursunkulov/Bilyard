package com.example.sonyadmin.bar.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sonyadmin.bar.category.BarViewModel
import com.example.sonyadmin.data.Repository.ProductRepository
import com.example.sonyadmin.util.Event
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class ProductViewModel(val productRepository: ProductRepository) : ViewModel() {
    var db = FirebaseFirestore.getInstance()
    private val TAG: String = BarViewModel::class.java.simpleName
    var products: MutableLiveData<List<Product>> = MutableLiveData(arrayListOf())

    val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarMessage: LiveData<Event<String>> = _snackbarText

    private val _dialog = MutableLiveData<Event<Boolean>>()
    val dialog: LiveData<Event<Boolean>> = _dialog

    fun getProducts(product: String) {
        _dataLoading.value = true

        productRepository.getProducts(product = product,
            onSuccess = {documentSnapshot ->
                var list = mutableListOf<Product>()
                documentSnapshot.data?.forEach {
                    val productDetails = Gson().fromJson(
                        Gson().toJson(it.value as HashMap<String, Long>), ProductDetails::class.java
                    )
                    val product1 = Product(it.key, productDetails)
                    list.add(product1)
                }
                products.postValue(list)

                _dataLoading.postValue(false)},
            onFailure = {})
    }

    fun addProductEvent(){
        _dialog.postValue(Event(true))
    }

    fun addProduct(product: Product){
        productRepository.addProduct(product)
        getProducts(product.name)
    }
}
