package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.utils.DATABASE_REPOSITORY
import kotlinx.coroutines.launch

class StartViewModel : ViewModel() {

    fun sendLoadUserDataRequest(onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            DATABASE_REPOSITORY.loadUserData {
                if (it != null) onSuccess()
                else onFailure()
            }
        }
    }
}