package com.example.sonyadmin.infoPerDay


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.sonyadmin.R
import kotlinx.android.synthetic.main.fragment_details.*
import org.joda.time.DateTime
import org.koin.android.viewmodel.ext.android.viewModel


class DailyInfoFragment : Fragment() {
    val viewModel by viewModel<DailyInfoViewModel>()
    val adapter = DailyInfoAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        cheeseList.adapter = adapter

        viewModel.getCash()

        viewModel.chees.observe(this, Observer {
            adapter.submitList(it)
        })

    }
}
