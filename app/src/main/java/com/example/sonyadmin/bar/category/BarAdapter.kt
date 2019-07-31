package com.example.sonyadmin.bar.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.example.sonyadmin.databinding.CategoryItemBinding

class BarAdapter(
    var data: List<Category>,
    val viewModel: BarViewModel
) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: CategoryItemBinding = getTaskItemBinding(convertView, parent)
        val userActionsListener = object : CategoryInterface {
            override fun onCategoryClicked(category: Category) {
                viewModel.openCategory(category)
            }

        }
        with(binding) {
            categor = data[position]
            listener = userActionsListener
        }
        return binding.root
    }

    override fun getItem(position: Int) = data[position]


    override fun getItemId(position: Int) = position.toLong()

    override fun getCount(): Int = data.size

    private fun getTaskItemBinding(view: View?, viewGroup: ViewGroup): CategoryItemBinding {
        return if (view == null) {
            // Inflate
            val inflater = LayoutInflater.from(viewGroup.context)
            CategoryItemBinding.inflate(inflater, viewGroup, false)
        } else {
            DataBindingUtil.getBinding(view) ?: throw IllegalStateException() as Throwable
        }
    }

    fun replaceData(items: List<Category>) {
        data = items
        notifyDataSetChanged()
    }
}