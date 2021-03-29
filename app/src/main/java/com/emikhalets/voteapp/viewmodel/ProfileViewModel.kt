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

    var profile_url = ""

    fun sendLogOutRequest(onComplete: () -> Unit) {
        viewModelScope.launch {
            AUTH_REPOSITORY.logOut { onComplete() }
        }
    }

    fun sendUserDataRequest(onSuccess: (User) -> Unit) {
        viewModelScope.launch {
            DATABASE_REPOSITORY.loadUserData {
                profile_url = it.photo
                onSuccess(it)
            }
        }
    }

    fun sendUpdateImageRequest(uri: Uri?, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            uri?.let {
                STORAGE_REPOSITORY.saveProfileImage(it) {
                    STORAGE_REPOSITORY.getProfileImageUrl { url ->
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