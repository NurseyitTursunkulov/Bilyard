package com.example.sonyadmin.gameList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.example.sonyadmin.databinding.GameItemBinding

class MyAdapter(var games: List<Game>, var tasksViewModel: MyModel) : BaseAdapter() {

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val binding: GameItemBinding
        binding = if (view == null) {
            val inflater = LayoutInflater.from(viewGroup?.context)
            GameItemBinding.inflate(inflater, viewGroup, false)
        } else {
            DataBindingUtil.getBinding(view) ?: throw IllegalStateException()
        }

        val userActionsListener = object : TaskItemUserActionsListener {
            override fun onCompleteChanged(game: Game, v: View) {
                tasksViewModel.endGame(game, position)
            }

            override fun onTaskClicked(ispressed: Boolean, game: Game) {
                if (ispressed)
                    tasksViewModel.startGame(game, position)
                else
                    tasksViewModel.endGame(game, position)
            }
        }

        with(binding) {
            Log.d("Main", "with binding")
            game = games[position]
            listener = userActionsListener
            executePendingBindings()
        }

        return binding.root

    }

    fun setList(games: List<Game>) {
        this.games = games
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Any = games[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = games.size

}