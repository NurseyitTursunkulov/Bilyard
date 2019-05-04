/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.sonyadmin.gameList

import android.widget.ListView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.example.sonyadmin.data.Task
import org.joda.time.DateTime


/**
 * Contains [BindingAdapter]s for the [Task] list.
 */
object TasksListBindings {

    @BindingAdapter("app:items")
    @JvmStatic fun setItems(listView: ListView, items:List<MutableLiveData<Task>>) {
        with(listView.adapter as MyAdapter) {
            setList(items)
        }
    }

    @BindingAdapter("app:txt")
    @JvmStatic fun dateToText(textView: TextView, hobbies: DateTime?) {
        textView.text = getTime(hobbies)
    }
    @BindingAdapter("app:ttx")
    @JvmStatic fun doubleToText(textView: TextView, hobbies: Double?) {
        textView.text = hobbies?.toString() ?: "0"
    }
    fun getTime(dateTime: DateTime?): String {
        if (dateTime == null) return "----"
        var h = dateTime.hourOfDay().get()
        var m = dateTime.minuteOfHour().get()
        return "${h.length()}:${m.length()}"
    }

    fun Int.length() = when(this) {
        in 0..9-> "0$this"
        else -> "$this"
    }
}
