package com.example.sonyadmin

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val viewModel = ViewModelProviders.of(this).get(MyModel::class.java)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
//            viewModel.refreshTitle()
            //Finally making the call

            val service = Apifactory.placeHolderApi
            GlobalScope.launch(Dispatchers.Main) {
                val postRequest = service.getUsers() // Making Network call

                try {
                    val response = postRequest.await()
                    Log.d("Main"," post = $response")
                    if (response.isSuccessful) {
//                        Do something with response e.g show to the UI.
                        val posts = response.body() // This is List<PlaceholderPosts>
                        Log.d("Main",posts.toString())
                    } else {
                        Log.d("Main","Error: ${response.code()}")
                    }

                }catch (e: Exception){
                    Log.d("Main", " exeption $e")
                }
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
