package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.ViewModel
import com.emikhalets.voteapp.data.AppRepository
import com.emikhalets.voteapp.data.AppRepository.Companion.get

class HomeViewModel : ViewModel() {
    private val repository: AppRepository?

    init {
        repository = get()
    }
}