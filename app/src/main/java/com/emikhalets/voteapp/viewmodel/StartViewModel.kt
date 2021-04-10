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

class StartViewModel @Inject constructor(
        private val authRepository: FirebaseAuthRepository,
        private val databaseRepository: FirebaseDatabaseRepository
) : ViewModel() {

    private val _userExisting = MutableLiveData<Boolean>()
    val userExisting get(): LiveData<Boolean> = _userExisting

    private val _error = MutableLiveData<Event<String>>()
    val error get(): LiveData<Event<String>> = _error

    fun checkUserExistingRequest() {
        viewModelScope.launch {
            checkUserUidThenLoadUserData()
        }
    }

    private fun checkUserUidThenLoadUserData() {
        viewModelScope.launch {
            authRepository.checkAuth { result ->
                when (result) {
                    is AppResult.Success -> loadUserData()
                    is AppResult.Error -> _error.postValue(Event(result.message))
                }
            }
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            databaseRepository.loadUserData { result ->
                when (result) {
                    is AppResult.Success -> _userExisting.postValue(true)
                    is AppResult.Error -> _error.postValue(Event(result.message))
                }
            }
        }
    }
}