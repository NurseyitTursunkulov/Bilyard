package com.example.sonyadmin.infoPerDay

import androidx.lifecycle.ViewModel
import androidx.paging.Config
import androidx.paging.toLiveData
import com.example.sonyadmin.data.Repository.Repository

class DailyInfoViewModel(var repository: Repository) : ViewModel() {
    var chees = repository.getCash().toLiveData(
        Config(
            pageSize = 30, enablePlaceholders = true,
            maxSize = 200
        )
    )


    fun getCash() {

        chees = repository.getCash().toLiveData(
            Config(
                pageSize = 30, enablePlaceholders = true,
                maxSize = 200
            )
        )
    }
}
