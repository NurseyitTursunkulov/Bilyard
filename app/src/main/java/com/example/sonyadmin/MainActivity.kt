package com.example.sonyadmin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.sonyadmin.databinding.ActivityMainBinding
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    //    private lateinit var viewDataBinding: ActivityMainBinding
    private lateinit var listAdapter: MyAdapter
    val model: MyModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var viewDataBinding = DataBindingUtil.
            setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            viewmodel = model
            setLifecycleOwner(this@MainActivity)
        }


        var list = listOf<Task>(
            Task("Nuts", 0), Task(id=1), Task("Nuts", 2), Task(id=3), Task(id=4), Task(
                "Nuts",
                5
            ), Task("wrt",6),Task("juuuh",7),Task(id=8)
        )

        model.items.value = list
        listAdapter = MyAdapter(ArrayList(0), model)
        viewDataBinding.tasksList.adapter = listAdapter


    }

}
