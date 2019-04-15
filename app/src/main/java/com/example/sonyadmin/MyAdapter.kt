package com.example.sonyadmin

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.InverseMethod
import androidx.lifecycle.MutableLiveData
import com.example.sonyadmin.databinding.TaskItemBinding
import org.joda.time.DateTime

class MyAdapter( var tasksViewModel: MyModel) : BaseAdapter() {
    var tasks: List<MutableLiveData<Task>> = tasksViewModel.items.value!!
    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val binding: TaskItemBinding
        binding = if (view == null) {
            // Inflate
            val inflater = LayoutInflater.from(viewGroup?.context)
            TaskItemBinding.inflate(inflater, viewGroup, false)
        } else {
            DataBindingUtil.getBinding(view) ?: throw IllegalStateException() as Throwable
        }

        val userActionsListener = object : TaskItemUserActionsListener {
            override fun onCompleteChanged(task: Task, v: View) {
                val checked = (v as CheckBox).isChecked
                tasksViewModel.completeTask(task)
            }

            override fun onTaskClicked(task: Task) {
                task.isPlaying.value?.let {
                    if (it){
                            tasksViewModel.openTask(task)
                    }

                    else
                        tasksViewModel.completeTask(task)
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

     fun setList(tasks: List<MutableLiveData<Task>>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Any = tasks[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = tasks.size

}


fun DateTime.getTime(): String {
    var h = DateTime.now().hourOfDay().get()
    var m = DateTime.now().secondOfMinute().get()
    return "${h.length()}:${m.length()}"
}

fun Int.length() = when(this) {
    in 0..9-> "0$this"
    else -> "$this"
}