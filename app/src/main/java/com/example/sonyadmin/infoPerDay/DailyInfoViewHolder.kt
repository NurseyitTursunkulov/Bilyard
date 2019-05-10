package com.example.sonyadmin.infoPerDay

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sonyadmin.R
import com.example.sonyadmin.data.DailyCount
import com.example.sonyadmin.data.Task
import com.example.sonyadmin.gameList.TasksListBindings.getTime

class DailyInfoViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
    (LayoutInflater.from(parent.context).inflate(R.layout.dayli_info_item, parent, false)) {
    private val date = itemView.findViewById<TextView>(R.id.date)
    private val sum = itemView.findViewById<TextView>(R.id.sum)
    var cheese: DailyCount? = null

    /**
     * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
     * ViewHolder when Item is loaded.
     */
    @SuppressLint("SetTextI18n")
    fun bindTo(cheese: DailyCount?) {
        this.cheese = cheese
        date.text = " ${cheese?.date?.year}/${cheese?.date?.monthOfYear}/" +
                "${cheese?.date?.dayOfMonth}"
        sum.text = cheese?.summ?.toString()
    }
}