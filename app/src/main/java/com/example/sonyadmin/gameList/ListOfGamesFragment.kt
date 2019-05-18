package com.example.sonyadmin.gameList


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.sonyadmin.R
import com.example.sonyadmin.databinding.FragmentListOfGamesBinding
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class ListOfGamesFragment : Fragment() {
    private lateinit var listAdapter: MyAdapter
    private lateinit var listItemBinding: FragmentListOfGamesBinding
    val model: MyModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        listItemBinding = DataBindingUtil.inflate<FragmentListOfGamesBinding>(
            layoutInflater,
            R.layout.fragment_list_of_games,
            container,
            false
        )
        listItemBinding.viewmodel = model
        listItemBinding.lifecycleOwner = this
        return listItemBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listAdapter = MyAdapter(ArrayList(0), model)
        listItemBinding.tasksList.adapter = listAdapter
        listItemBinding.viewmodel?.showToast?.observe(this, androidx.lifecycle.Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this@ListOfGamesFragment.context, it, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
