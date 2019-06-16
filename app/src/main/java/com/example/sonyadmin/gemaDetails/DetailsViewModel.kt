package com.example.sonyadmin.gemaDetails

import androidx.lifecycle.ViewModel
import androidx.paging.Config
import androidx.paging.toLiveData
import com.example.sonyadmin.data.Repository.Repository

class DetailsViewModel(var repository: Repository) : ViewModel() {
    var chees = repository.getGameDetails(0).toLiveData(
        Config(
            pageSize = 30, enablePlaceholders = true,
            maxSize = 200
        )
    )


    fun getAllGamesOfCabin(task: Int) {

        chees = repository.getGameDetails(task).toLiveData(
            Config(
                pageSize = 30, enablePlaceholders = true,
                maxSize = 200
            )
        )
    }
}
