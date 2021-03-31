package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.DATABASE_REPOSITORY
import kotlinx.coroutines.launch

class TopUsersViewModel : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users get():LiveData<List<User>> = _users

    fun sendLoadTopUsersRequest() {
        if (_users.value.isNullOrEmpty()) {
            viewModelScope.launch {
                DATABASE_REPOSITORY.loadTopUsers {
                    if (it.isNotEmpty()) {
                        val images = it.sortedByDescending { item -> item.rating }
                        _users.postValue(images)
                    }
                }
            }
        }
    }
}