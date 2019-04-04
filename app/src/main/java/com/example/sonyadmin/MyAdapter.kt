package com.example.sonyadmin

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import androidx.databinding.DataBindingUtil
import com.example.sonyadmin.databinding.TaskItemBinding

class MyAdapter(var tasks: List<Task>, var tasksViewModel: MyModel) : BaseAdapter() {

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val binding: TaskItemBinding
        binding = if (view == null) {
            val inflater = LayoutInflater.from(viewGroup?.context)
            Log.d("Main", "bincding if view == null" )
            TaskItemBinding.inflate(inflater, viewGroup, false)
        } else {
            Log.d("Main", "bincding if view == null else " )
            DataBindingUtil.getBinding(view) ?: throw IllegalStateException()
        }

        val userActionsListener = object : TaskItemUserActionsListener {
            override fun onCompleteChanged(task: Task, v: View) {
                val checked = (v as CheckBox).isChecked
                tasksViewModel.completeTask(task, checked)
            }

            override fun onTaskClicked(task: Task) {
                tasksViewModel.openTask(task.id)
            }
        }

        with(binding) {
            Log.d("Main", "with binding" )
            task = tasks[position]
            listener = userActionsListener
            executePendingBindings()
        }

        return binding.root

    }

     fun setList(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Any = tasks[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = tasks.size

}