package com.emikhalets.voteapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.*
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user get():LiveData<User> = _user

    var profileUrl = ""
        private set

    fun sendLogOutRequest(onComplete: () -> Unit) {
        viewModelScope.launch {
            AUTH_REPOSITORY.logOut {
                onComplete()
            }
        }
    }

    fun sendLoadUserDataRequest(onSuccess: (User) -> Unit) {
        if (_user.value == null || _user.value?.id == "") {
            viewModelScope.launch {
                DATABASE_REPOSITORY.loadUserData {
                    profileUrl = it.photo
                    _user.postValue(it)
                    onSuccess(it)
                }
            }
        }
    }

    fun sendUpdateUserPhotoRequest(uri: Uri?) {
        uri?.let {
            viewModelScope.launch {
                STORAGE_REPOSITORY.saveUserPhoto(it) {
                    STORAGE_REPOSITORY.loadUserPhotoUrl { url ->
                        AUTH_REPOSITORY.updateUserPhoto(url) {
                            DATABASE_REPOSITORY.updateUserPhoto(url) {
                                val newUser = _user.value ?: User()
                                newUser.photo = USER_PHOTO
                                profileUrl = USER_PHOTO
                                _user.postValue(newUser)
                            }
                        }
                    }
                }
            }
        }
    }

    fun sendUpdatePassRequest(pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            AUTH_REPOSITORY.updateUserPassword(pass) { onSuccess() }
        }
    }

    fun sendUpdateUsernameRequest(name: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            AUTH_REPOSITORY.updateUsername(name) {
                DATABASE_REPOSITORY.updateUsername(name) {
                    val newUser = _user.value ?: User()
                    newUser.username = USERNAME
                    _user.postValue(newUser)
                    onSuccess()
                }
            }
        }
    }
}