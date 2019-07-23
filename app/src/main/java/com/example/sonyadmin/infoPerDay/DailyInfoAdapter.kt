package com.example.sonyadmin.infoPerDay

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.sonyadmin.data.DailyCount


class DailyInfoAdapter : PagedListAdapter<DailyCount, DailyInfoViewHolder>(diffCallback) {
    override fun onBindViewHolder(holder: DailyInfoViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyInfoViewHolder =
        DailyInfoViewHolder(parent)

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<DailyCount>() {
            override fun areItemsTheSame(oldItem: DailyCount, newItem: DailyCount): Boolean =
                oldItem.date.dayOfYear == newItem.date.dayOfYear

            override fun areContentsTheSame(oldItem: DailyCount, newItem: DailyCount): Boolean =
                oldItem == newItem
        }
    }
}