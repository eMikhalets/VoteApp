package com.emikhalets.voteapp.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.AUTH_REPOSITORY
import com.emikhalets.voteapp.utils.DATABASE_REPOSITORY
import com.emikhalets.voteapp.utils.STORAGE_REPOSITORY
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    var profileUrl = ""
        private set

    fun sendLogOutRequest(onComplete: () -> Unit) {
        viewModelScope.launch {
            AUTH_REPOSITORY.logOut { onComplete() }
        }
    }

    fun sendUserDataRequest(onSuccess: (User) -> Unit) {
        viewModelScope.launch {
            DATABASE_REPOSITORY.loadUserData {
                profileUrl = it.photo
                onSuccess(it)
            }
        }
    }

    fun sendUpdateProfileImageRequest(uri: Uri?, onSuccess: (String) -> Unit) {
        uri?.let {
            viewModelScope.launch {
                STORAGE_REPOSITORY.saveProfileImage(it) {
                    STORAGE_REPOSITORY.loadProfileImageUrl { url ->
                        DATABASE_REPOSITORY.updateUserPhoto(url) {
                            onSuccess(url)
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
}