package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.firebase.FirebaseAuthRepository
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
        private val authRepository: FirebaseAuthRepository,
        private val databaseRepository: FirebaseDatabaseRepository,
) : ViewModel() {

    fun sendLoginRequest(
            login: String,
            pass: String,
            onSuccess: () -> Unit,
            onFailure: () -> Unit,
            onAuthFailure: () -> Unit,
    ) {
        viewModelScope.launch {
            authRepository.login(login, pass, {
                databaseRepository.loadUserData {
                    if (it != null) onSuccess()
                    else onFailure()
                }
            }, { onAuthFailure() })
        }
    }
}