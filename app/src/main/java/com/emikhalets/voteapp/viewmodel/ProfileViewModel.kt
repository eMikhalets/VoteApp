package com.emikhalets.voteapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.AUTH_REPOSITORY
import com.emikhalets.voteapp.utils.DATABASE_REPOSITORY
import com.emikhalets.voteapp.utils.STORAGE_REPOSITORY
import com.emikhalets.voteapp.utils.USER
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user get():LiveData<User> = _user

    fun sendLogOutRequest(onComplete: () -> Unit) {
        viewModelScope.launch {
            AUTH_REPOSITORY.logOut {
                _user.value = User()
                onComplete()
            }
        }
    }

    fun sendLoadUserDataRequest(onFailure: () -> Unit) {
        if (_user.value == null || _user.value?.id == "") {
            viewModelScope.launch {
                DATABASE_REPOSITORY.loadUserData {
                    if (it != null) _user.postValue(it)
                    else onFailure()
                }
            }
        }
    }

    fun sendUpdateUserPhotoRequest(uri: Uri?) {
        uri?.let {
            viewModelScope.launch {
                STORAGE_REPOSITORY.saveUserPhoto(it) {
                    STORAGE_REPOSITORY.loadUserPhotoUrl { url ->
                        DATABASE_REPOSITORY.updateUserPhoto(url) {
                            val newUser = USER
                            _user.postValue(newUser)
                        }
                    }
                }
            }
        }
    }

    fun sendUpdatePassRequest(pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            AUTH_REPOSITORY.updateUserPassword(pass) {
                onSuccess()
            }
        }
    }

    fun sendUpdateUsernameRequest(name: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            DATABASE_REPOSITORY.updateUsername(name) {
                val newUser = USER
                _user.postValue(newUser)
                onSuccess()
            }
        }
    }
}