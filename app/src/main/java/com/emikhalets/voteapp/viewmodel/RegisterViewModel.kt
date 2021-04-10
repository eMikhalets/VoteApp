package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.firebase.FirebaseAuthRepository
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import com.emikhalets.voteapp.utils.AppResult
import com.emikhalets.voteapp.utils.Event
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
        private val authRepository: FirebaseAuthRepository,
        private val databaseRepository: FirebaseDatabaseRepository
) : ViewModel() {

    private val _registerState = MutableLiveData<Boolean>()
    val registerState get(): LiveData<Boolean> = _registerState

    private val _error = MutableLiveData<Event<String>>()
    val error get(): LiveData<Event<String>> = _error

    fun sendRegisterRequest(login: String, pass: String) {
        viewModelScope.launch {
            registerThenSaveUser(login, pass)
        }
    }

    private fun registerThenSaveUser(login: String, pass: String) {
        viewModelScope.launch {
            authRepository.register(login, pass) { result ->
                when (result) {
                    is AppResult.Success -> saveUserToDatabase(login)
                    is AppResult.Error -> _error.postValue(Event(result.message))
                }
            }
        }
    }

    private fun saveUserToDatabase(login: String) {
        viewModelScope.launch {
            databaseRepository.saveUser(login) { result ->
                when (result) {
                    is AppResult.Success -> _registerState.postValue(true)
                    is AppResult.Error -> _error.postValue(Event(result.message))
                }
            }
        }
    }
}