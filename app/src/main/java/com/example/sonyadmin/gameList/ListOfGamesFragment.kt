package com.example.sonyadmin.gameList


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.sonyadmin.MyCoroutineWorker
import com.example.sonyadmin.R
import com.example.sonyadmin.databinding.FragmentListOfGamesBinding
import me.nikhilchaudhari.asynkio.core.async
import me.nikhilchaudhari.asynkio.core.request
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
    }
}
