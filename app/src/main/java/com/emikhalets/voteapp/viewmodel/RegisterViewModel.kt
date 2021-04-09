package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.firebase.FirebaseAuthRepository
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
        private val authRepository: FirebaseAuthRepository,
        private val databaseRepository: FirebaseDatabaseRepository,
) : ViewModel() {

    fun sendRegisterRequest(login: String, pass: String, complete: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            authRepository.register(login, pass) { isSuccess, error ->
                if (isSuccess) {
                    suspend {
                        databaseRepository.saveUser(login) { isSaveSuccess, saveError ->
                            complete(isSaveSuccess, saveError)
                        }
                    }
                } else {
                    complete(false, error)
                }
            }
        }
    }
}