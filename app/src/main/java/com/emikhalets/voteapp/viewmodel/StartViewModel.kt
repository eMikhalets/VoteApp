package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class StartViewModel @Inject constructor(
        private val databaseRepository: FirebaseDatabaseRepository,
) : ViewModel() {

    fun sendLoadUserDataRequest(onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            databaseRepository.loadUserData { user, _ ->
                if (user != null) onSuccess()
                else onFailure()
            }
        }
    }
}