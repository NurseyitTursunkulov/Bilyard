package com.example.sonyadmin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.sonyadmin.databinding.ActivityMainBinding
import com.example.sonyadmin.gameList.MyAdapter
import com.example.sonyadmin.gameList.MyModel
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


//        var list = listOf<Game>(
//            Game("Nuts", 0), Game(id=1), Game("Nuts", 2), Game(id=3), Game(id=4), Game(
//                "Nuts",
//                5
//            ), Game("wrt",6),Game("juuuh",7),Game(id=8)
//        )

//        model.items.value = list
        listAdapter = MyAdapter(ArrayList(0), model)
        viewDataBinding.tasksList.adapter = listAdapter


    }

}
