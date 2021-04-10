package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import com.emikhalets.voteapp.utils.AppResult
import com.emikhalets.voteapp.utils.Event
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopUsersViewModel @Inject constructor(
        private val databaseRepository: FirebaseDatabaseRepository,
) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users get():LiveData<List<User>> = _users

    private val _error = MutableLiveData<Event<String>>()
    val error get(): LiveData<Event<String>> = _error

    fun sendLoadTopUsersRequest() {
        viewModelScope.launch {
            databaseRepository.loadTopUsers { result ->
                when (result) {
                    is AppResult.Success -> {
                        val list = result.data.sortedByDescending { it.rating }
                        _users.postValue(list)
                    }
                    is AppResult.Error -> _error.postValue(Event(""))
                }
            }
        }
    }
}