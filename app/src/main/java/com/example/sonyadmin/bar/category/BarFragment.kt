package com.example.sonyadmin.bar.category


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.sonyadmin.R
import com.example.sonyadmin.databinding.FragmentBarBinding
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class BarFragment : Fragment() {

    private lateinit var binding: FragmentBarBinding
    val model: BarViewModel by viewModel()
    lateinit var barAdapter: BarAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentBarBinding>(layoutInflater, R.layout.fragment_bar, container, false)
            .apply {
                viewmodel = model
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner
        setupListAdapter()

    }

    private fun setupListAdapter() {
        val viewModel = binding.viewmodel
        if (viewModel != null) {
            barAdapter = BarAdapter(ArrayList(0), viewModel)
            binding.tasksList.adapter = barAdapter
        } else {
            Log.d("Main","ViewModel not initialized when attempting to set up adapter.")
        }
    }
}
