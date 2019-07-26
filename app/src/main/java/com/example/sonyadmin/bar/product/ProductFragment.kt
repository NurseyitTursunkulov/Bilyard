package com.example.sonyadmin.bar.product


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.sonyadmin.R
import com.example.sonyadmin.databinding.FragmentProductBinding
import com.example.sonyadmin.gameList.MyModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    val productViewModel: ProductViewModel by viewModel()
    val gameListViewModel : MyModel by sharedViewModel()
    lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentProductBinding>(layoutInflater, R.layout.fragment_product, container, false)
            .apply {
                viewmodel = productViewModel
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner

        arguments?.let { bundle ->
            val categoryName = ProductFragmentArgs.fromBundle(bundle).categoryName
            val cabinId = ProductFragmentArgs.fromBundle(bundle).cabinId
            Log.d("Main", categoryName)
            setupListAdapter(cabinId)
            var db = categoryName?.let { product ->
                binding.viewmodel?.getProducts(product)
            }
        }
    }

    private fun setupListAdapter(cabinId: Int) {
        val viewModel = binding.viewmodel
        if (viewModel != null) {
            productAdapter = ProductAdapter(ArrayList(0), viewModel,gameListViewModel,cabinId)
            binding.tasksList.adapter = productAdapter
            viewModel.products.observe(this, Observer {
                Log.d("Main", "products $it")
            })
        } else {
            Log.d("Main", "ViewModel not initialized when attempting to set up adapter.")
        }
    }


}
