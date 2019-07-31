package com.example.sonyadmin.bar.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.sonyadmin.databinding.CategoryItemBinding
import com.example.sonyadmin.databinding.ProductItemBinding
import com.example.sonyadmin.gameList.ListOfGamesFragmentDirections
import com.example.sonyadmin.gameList.MyModel
import com.example.sonyadmin.util.Event

class ProductAdapter(
    var data: List<Product>,
    val viewModel: ProductViewModel,
    val gameViewModel: MyModel,
    val cabinId:Int
) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ProductItemBinding = getProductItemBinding(convertView, parent)
        val userActionsListener = object : ProductInterface {
            override fun increase() {

            }

            override fun decrease() {
            }

            override fun onProductClicked(product: Product) {
                gameViewModel.addBar(cabinId,product)
                viewModel._snackbarText.postValue(Event(product.name))
//                Navigation.findNavController(binding.btnAdd).navigate(
//                    ProductFragmentDirections.actionProductFragmentToListOfGamesFragment2()
//                )
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