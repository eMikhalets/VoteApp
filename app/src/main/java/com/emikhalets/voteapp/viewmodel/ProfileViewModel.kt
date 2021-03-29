package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.utils.AUTH_REPOSITORY
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    fun sendLogOutRequest(onComplete: () -> Unit) {
        viewModelScope.launch {
            AUTH_REPOSITORY.logOut { onComplete() }
        }
    }
}