package com.example.sonyadmin.gemaDetails

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sonyadmin.R
import com.example.sonyadmin.data.Task
import com.example.sonyadmin.gameList.TasksListBindings.getTime
import org.w3c.dom.Text
import java.util.ArrayList

class GameDetailViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
    (LayoutInflater.from(parent.context).inflate(R.layout.cheese_item, parent, false)) {
    private val startTime = itemView.findViewById<TextView>(R.id.date)
    private val endTime = itemView.findViewById<TextView>(R.id.end_time)
    private val sum = itemView.findViewById<TextView>(R.id.sum)
    private val userName = itemView.findViewById<TextView>(R.id.user_name)
    private val barTextView = itemView.findViewById<TextView>(R.id.bar_txt)
    private val barSum = itemView.findViewById<TextView>(R.id.summ_txt)
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
        userName.text = cheese?.userName
        barTextView.setSingleLine(false)
       cheese?.listOfBars?.forEach {
           barTextView.append(it.name + "\n")
           barSum.append( "${it.details.price}" + "\n")
       }
    }
}
