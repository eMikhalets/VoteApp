package com.emikhalets.voteapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.model.firebase.FirebaseAuthRepository
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import com.emikhalets.voteapp.model.firebase.FirebaseStorageRepository
import com.emikhalets.voteapp.utils.USER
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
        private val authRepository: FirebaseAuthRepository,
        private val databaseRepository: FirebaseDatabaseRepository,
        private val storageRepository: FirebaseStorageRepository,
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user get():LiveData<User> = _user

    fun sendLogOutRequest(onComplete: () -> Unit) {
        viewModelScope.launch {
            authRepository.logOut {
                _user.value = User()
                onComplete()
            }
        }
    }

    fun sendLoadUserDataRequest(onFailure: () -> Unit) {
        if (_user.value == null || _user.value?.id == "") {
            viewModelScope.launch {
                databaseRepository.loadUserData {
                    if (it != null) _user.postValue(it)
                    else onFailure()
                }
            }
        }
    }

    fun sendUpdateUserPhotoRequest(uri: Uri?) {
        uri?.let {
            viewModelScope.launch {
                storageRepository.saveUserPhoto(it) { url ->
                    databaseRepository.updateUserPhoto(url) {
                        val newUser = USER
                        _user.postValue(newUser)
                    }
                }
            }
        }
    }

    fun sendUpdatePassRequest(pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            authRepository.updateUserPassword(pass) {
                onSuccess()
            }
        }
    }

    fun sendUpdateUsernameRequest(name: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            databaseRepository.updateUsername(name) {
                val newUser = USER
                _user.postValue(newUser)
                onSuccess()
            }
        }
    }
}