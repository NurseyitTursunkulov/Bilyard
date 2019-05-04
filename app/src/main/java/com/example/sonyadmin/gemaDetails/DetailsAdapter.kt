package com.example.sonyadmin.gemaDetails

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.sonyadmin.data.Task


class DetailsAdapter : PagedListAdapter<Task, GameDetailViewHolder>(diffCallback) {
    override fun onBindViewHolder(holder: GameDetailViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameDetailViewHolder =
        GameDetailViewHolder(parent)

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
                oldItem == newItem
        }
    }
}