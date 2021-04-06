package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class StartViewModel @Inject constructor(
        private val databaseRepository: FirebaseDatabaseRepository,
) : ViewModel() {

    fun sendLoadUserDataRequest(onComplete: (success: Boolean, error: String) -> Unit) {
        viewModelScope.launch {
            databaseRepository.loadUserData { user, error ->
                if (user != null) onComplete(true, "")
                else onComplete(false, error)
            }
        }
    }
}