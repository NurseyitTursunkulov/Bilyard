package com.example.sonyadmin.gemaDetails

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sonyadmin.R
import com.example.sonyadmin.data.Task
import com.example.sonyadmin.gameList.TasksListBindings.getTime

class GameDetailViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
    (LayoutInflater.from(parent.context).inflate(R.layout.cheese_item, parent, false)) {
    private val startTime = itemView.findViewById<TextView>(R.id.date)
    private val endTime = itemView.findViewById<TextView>(R.id.end_time)
    private val sum = itemView.findViewById<TextView>(R.id.sum)
    var cheese: Task? = null

    /**
     * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
     * ViewHolder when Item is loaded.
     */
    @SuppressLint("SetTextI18n")
    fun bindTo(cheese: Task?) {
        this.cheese = cheese
        startTime.text = getTime(cheese?.startTime)
        endTime.text = getTime(cheese?.endTime) +
                " ${cheese?.endTime?.year}/${cheese?.endTime?.monthOfYear}/" +
                "${cheese?.endTime?.dayOfMonth}"
        sum.text = cheese?.summ.toString()
    }
}