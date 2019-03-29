package com.example.sonyadmin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.sonyadmin.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    //    private lateinit var viewDataBinding: ActivityMainBinding
    private lateinit var listAdapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model = ViewModelProviders.of(this).get(MyModel::class.java)
        var viewDataBinding = DataBindingUtil.
            setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            viewmodel = model
            setLifecycleOwner(this@MainActivity)
        }


        var list = listOf<Task>(
            Task("Nuts", 5), Task(),
            Task(), Task("Nuts", 5), Task(), Task(), Task(
                "Nuts",
                5
            ), Task(), Task(), Task(),
            Task(), Task("Nuts", 5), Task(), Task(), Task(
                "Nuts",
                5
            ), Task(), Task()
        )

        model.items.value = list
        listAdapter = MyAdapter(ArrayList(0), model)
        viewDataBinding.tasksList.adapter = listAdapter


    }

    override fun onResume() {
        super.onResume()
//        viewDataBinding.viewmodel?.start()
    }
}
