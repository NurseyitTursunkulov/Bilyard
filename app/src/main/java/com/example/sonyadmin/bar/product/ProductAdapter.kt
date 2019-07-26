package com.example.sonyadmin.bar.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.example.sonyadmin.databinding.CategoryItemBinding
import com.example.sonyadmin.databinding.ProductItemBinding

class ProductAdapter(
    var data: List<Product>,
    val viewModel: ProductViewModel
) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ProductItemBinding = getProductItemBinding(convertView, parent)
        val userActionsListener = object : ProductInterface {
            override fun increase() {

            }

            override fun decrease() {
            }

            override fun onProductClicked(product: Product) {
//                viewModel.openCategory(category)
            }

        }
        with(binding) {
            product = data[position]
            listener = userActionsListener
        }
        return binding.root
    }

    override fun getItem(position: Int) = data[position]


    override fun getItemId(position: Int) = position.toLong()

    override fun getCount(): Int = data.size

    private fun getProductItemBinding(view: View?, viewGroup: ViewGroup): ProductItemBinding {
        return if (view == null) {
            // Inflate
            val inflater = LayoutInflater.from(viewGroup.context)
            ProductItemBinding.inflate(inflater, viewGroup, false)
        } else {
            DataBindingUtil.getBinding(view) ?: throw IllegalStateException() as Throwable
        }
    }

    fun replaceData(items: List<Product>) {
        data = items
        notifyDataSetChanged()
    }
}