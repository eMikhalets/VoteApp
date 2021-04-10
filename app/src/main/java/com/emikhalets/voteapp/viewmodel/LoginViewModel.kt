package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.firebase.FirebaseAuthRepository
import com.emikhalets.voteapp.utils.AppResult
import com.emikhalets.voteapp.utils.Event
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
        private val authRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _loginState = MutableLiveData<Boolean>()
    val loginState get(): LiveData<Boolean> = _loginState

    private val _error = MutableLiveData<Event<String>>()
    val error get(): LiveData<Event<String>> = _error

    fun sendLoginRequest(login: String, pass: String) {
        viewModelScope.launch {
            authRepository.login(login, pass) { result ->
                when (result) {
                    is AppResult.Success -> _loginState.postValue(true)
                    is AppResult.Error -> _error.postValue(Event(result.message))
                }
            }
        }
    }
}