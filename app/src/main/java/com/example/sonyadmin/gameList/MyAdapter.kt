package com.example.sonyadmin.gameList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation.findNavController
import com.example.sonyadmin.data.Task
import com.example.sonyadmin.databinding.TaskItemBinding

class MyAdapter(private var tasks: List<LiveData<Task>>, var tasksViewModel: MyModel) : BaseAdapter() {

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {

        val binding: TaskItemBinding = getTaskItemBinding(view, viewGroup)

        val userActionsListener = object : TaskItemUserActionsListener {
            override fun deleteAll(taskId: Int) {

                findNavController(binding.textView3).navigate(
                            ListOfGamesFragmentDirections.actionListOfGamesFragmentToDetailsFragment(taskId)
                )
            }

            override fun onCompleteChanged(task: Task, v: View) {
                tasksViewModel.completeTask(task)
            }

            override fun onTaskClicked(task: Task) {
                task.isPlaying?.let {
                    if (it){
                        tasksViewModel.completeTask(task)
                    }
                    else
                        tasksViewModel.openTask(task)
                }
                binding.executePendingBindings()
                notifyDataSetChanged()
            }
        }

        with(binding) {
            task = tasks[position].value
            listener = userActionsListener
            executePendingBindings()
        }

        return binding.root

    }

     fun setList(tasks: List<LiveData<Task>>) {
        this.tasks = tasks
        notifyDataSetChanged()
         Log.d("Adapter","setList")
    }

    override fun getItem(position: Int): Any = tasks[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = tasks.size

    private fun getTaskItemBinding(view: View?, viewGroup: ViewGroup?): TaskItemBinding {
        return if (view == null) {
            // Inflate
            val inflater = LayoutInflater.from(viewGroup?.context)
            TaskItemBinding.inflate(inflater, viewGroup, false)
        } else {
            DataBindingUtil.getBinding(view) ?: throw IllegalStateException() as Throwable
        }
    }

}


