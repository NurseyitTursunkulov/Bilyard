package com.example.sonyadmin.gemaDetails


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.sonyadmin.R
import kotlinx.android.synthetic.main.fragment_details.*
import org.koin.android.viewmodel.ext.android.viewModel


class DetailsFragment : Fragment() {
    val viewModel by viewModel<DetailsViewModel>()
    val args: DetailsFragmentArgs by navArgs()
    val adapter = DetailsAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        cheeseList.adapter = adapter

        viewModel.getAllGamesOfCabin(args.cabinId)

        viewModel.chees.observe(this, Observer {
            adapter.submitList(it)
        })

    }
}
