package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopUsersViewModel @Inject constructor(
        private val databaseRepository: FirebaseDatabaseRepository,
) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users get():LiveData<List<User>> = _users

    fun sendLoadTopUsersRequest() {
        viewModelScope.launch {
            databaseRepository.loadTopUsers {
                if (it.isNotEmpty()) {
                    val images = it.sortedByDescending { item -> item.rating }
                    _users.postValue(images)
                }
            }
        }
    }
}