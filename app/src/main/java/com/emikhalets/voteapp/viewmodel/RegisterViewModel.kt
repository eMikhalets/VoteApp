package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.utils.AUTH_REPOSITORY
import com.emikhalets.voteapp.utils.DATABASE_REPOSITORY
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    fun sendRegisterRequest(login: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            AUTH_REPOSITORY.register(login, pass) {
                DATABASE_REPOSITORY.saveUserToDatabase(login, pass) {
                    DATABASE_REPOSITORY.fillCurrentUserData { onSuccess() }
                }
            }
        }
    }
}