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


        model.items.value?.forEach {
            it.observe(this,androidx.lifecycle.Observer {
                it.startTime.observe(this,androidx.lifecycle.Observer {
                    Log.d("Main","startTime ${it}")
                })
                Log.d("Main","changed ${it.idForTitle}")
            })
        }
        listAdapter = MyAdapter( model)
        viewDataBinding.tasksList.adapter = listAdapter


    }

}
