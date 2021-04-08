package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import com.emikhalets.voteapp.utils.AppValueEventListener
import com.emikhalets.voteapp.utils.toUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
        private val databaseRepository: FirebaseDatabaseRepository,
) : ViewModel() {

    private val _images = MutableLiveData<List<Image>>()
    val images get():LiveData<List<Image>> = _images

    private val _user = MutableLiveData<User>()
    val user get():LiveData<User> = _user

    private lateinit var userReference: DatabaseReference
    private lateinit var userDataListener: ValueEventListener

    override fun onCleared() {
        super.onCleared()
        userReference.removeEventListener(userDataListener)
    }

    fun sendLoadUserDataRequest() {
        viewModelScope.launch {
            userReference = databaseRepository.listenUserDataChanges()
            userDataListener = AppValueEventListener { _user.postValue(it.toUser()) }
            userReference.addValueEventListener(userDataListener)
        }
    }

    fun sendLatestImagesRequest(isRefresh: Boolean = false) {
        if (_images.value == null || isRefresh) {
            viewModelScope.launch {
                databaseRepository.loadLatestImages { images ->
                    val list = images.sortedByDescending { it.timestamp }
                    _images.postValue(list)
                }
            }
        }
    }
}