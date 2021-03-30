package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.utils.DATABASE_REPOSITORY
import kotlinx.coroutines.launch

class StartViewModel : ViewModel() {

    fun sendCheckUserExistRequest(onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            DATABASE_REPOSITORY.checkUserExisting {
                onSuccess(it)
            }
        }
    }
}