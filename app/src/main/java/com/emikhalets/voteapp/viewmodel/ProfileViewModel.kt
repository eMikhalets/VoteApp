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
import com.emikhalets.voteapp.utils.AppValueEventListener
import com.emikhalets.voteapp.utils.toUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
        private val authRepository: FirebaseAuthRepository,
        private val databaseRepository: FirebaseDatabaseRepository,
        private val storageRepository: FirebaseStorageRepository,
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user get():LiveData<User> = _user

    private val userReference: DatabaseReference = databaseRepository.listenUserDataChanges()

    private val userDataListener: ValueEventListener = AppValueEventListener {
        _user.postValue(it.toUser())
    }

    override fun onCleared() {
        super.onCleared()
        userReference.removeEventListener(userDataListener)
    }

    fun sendLoadUserDataRequest() {
        if (_user.value == null || _user.value?.id == "") {
            viewModelScope.launch {
                userReference.addValueEventListener(userDataListener)
            }
        }
    }

    fun sendLogOutRequest(onComplete: () -> Unit) {
        viewModelScope.launch {
            authRepository.logOut {
                _user.value = User()
                onComplete()
            }
        }
    }

    fun sendUpdateUserPhotoRequest(uri: Uri?, onComplete: (success: Boolean, error: String) -> Unit) {
        uri?.let {
            viewModelScope.launch {
                storageRepository.saveUserPhoto(it) { isStorageSuccess, url, storageError ->
                    if (isStorageSuccess) {
                        suspend {
                            authRepository.updateUserPhoto(url) { isAuthSuccess, authError ->
                                if (isAuthSuccess) {
                                    suspend {
                                        databaseRepository.updateUserPhoto(url) { isSuccess: Boolean, error ->
                                            if (isSuccess) onComplete(true, "")
                                            else onComplete(false, error)
                                        }
                                    }
                                } else {
                                    onComplete(false, authError)
                                }
                            }
                        }
                    } else {
                        onComplete(false, storageError)
                    }
                }
            }
        }
    }

    fun sendUpdatePassRequest(pass: String, onComplete: (success: Boolean, error: String) -> Unit) {
        viewModelScope.launch {
            authRepository.updateUserPassword(pass) { isSuccess, error ->
                if (isSuccess) {
                    onComplete(true, "")
                } else {
                    onComplete(false, error)
                }
            }
        }
    }

    fun sendUpdateUsernameRequest(name: String, onComplete: (success: Boolean, error: String) -> Unit) {
        viewModelScope.launch {
            authRepository.updateUsername(name) { isAuthSuccess, authError ->
                if (isAuthSuccess) {
                    suspend {
                        databaseRepository.updateUsername(name) { isSuccess, error ->
                            if (isSuccess) onComplete(true, "")
                            else onComplete(false, error)
                        }
                    }
                } else {
                    onComplete(false, authError)
                }
            }
        }
    }
}