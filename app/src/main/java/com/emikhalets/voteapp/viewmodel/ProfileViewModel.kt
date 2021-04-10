package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.model.firebase.FirebaseAuthRepository
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import com.emikhalets.voteapp.model.firebase.FirebaseStorageRepository
import com.emikhalets.voteapp.utils.AppResult
import com.emikhalets.voteapp.utils.AppValueEventListener
import com.emikhalets.voteapp.utils.Event
import com.emikhalets.voteapp.utils.toUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
        private val authRepository: FirebaseAuthRepository,
        private val databaseRepository: FirebaseDatabaseRepository,
        private val storageRepository: FirebaseStorageRepository
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user get(): LiveData<User> = _user

    private val _saveImageState = MutableLiveData<String>()
    val saveImageState get(): LiveData<String> = _saveImageState

    private val _usernameState = MutableLiveData<Boolean>()
    val usernameState get(): LiveData<Boolean> = _usernameState

    private val _passwordState = MutableLiveData<Boolean>()
    val passwordState get(): LiveData<Boolean> = _passwordState

    private val _logoutState = MutableLiveData<Boolean>()
    val logoutState get(): LiveData<Boolean> = _logoutState

    private val _error = MutableLiveData<Event<String>>()
    val error get(): LiveData<Event<String>> = _error

    // Требуется инициализация этих преременных, так как в диалоге используется эта же вьюмодель,
    // а при выходе из диалога идет обращение в onCleared к неинициализированной userReference,
    // и делать отдельные вьюмодели для кажного диалога мне лень
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

    fun sendLogOutRequest() {
        viewModelScope.launch {
            authRepository.logOut {
                _logoutState.postValue(true)
            }
        }
    }

    fun sendUpdateUserPhotoRequest(uri: String) {
        viewModelScope.launch {
            if (uri.isNotEmpty()) saveUserPhotoInStorage(uri)
            else _error.postValue(Event("Image Uri is empty"))
        }
    }

    private fun saveUserPhotoInStorage(uri: String) {
        viewModelScope.launch {
            storageRepository.saveUserPhoto(uri) { result ->
                when (result) {
                    is AppResult.Error -> _error.postValue(Event(result.message))
                    is AppResult.Success -> updateUserPhotoInAuth(result.data)
                }
            }
        }
    }

    private fun updateUserPhotoInAuth(url: String) {
        viewModelScope.launch {
            authRepository.updateUserPhoto(url) { result ->
                when (result) {
                    is AppResult.Error -> _error.postValue(Event(result.message))
                    is AppResult.Success -> updateUserPhotoInDatabase(url)
                }
            }
        }
    }

    private fun updateUserPhotoInDatabase(url: String) {
        viewModelScope.launch {
            databaseRepository.updateUserPhoto(url) { result ->
                when (result) {
                    is AppResult.Error -> _error.postValue(Event(result.message))
                    is AppResult.Success -> _saveImageState.postValue(url)
                }
            }
        }
    }

    fun sendUpdatePassRequest(pass: String) {
        viewModelScope.launch {
            authRepository.updateUserPassword(pass) { result ->
                when (result) {
                    is AppResult.Error -> _error.postValue(Event(result.message))
                    is AppResult.Success -> _passwordState.postValue(true)
                }
            }
        }
    }

    fun sendUpdateUsernameRequest(name: String) {
        viewModelScope.launch {
            authRepository.updateUsername(name) { result ->
                when (result) {
                    is AppResult.Error -> _error.postValue(Event(result.message))
                    is AppResult.Success -> updateUsernameInDatabase(name)
                }
            }
        }
    }

    private fun updateUsernameInDatabase(name: String) {
        viewModelScope.launch {
            databaseRepository.updateUsername(name) { result ->
                when (result) {
                    is AppResult.Error -> _error.postValue(Event(result.message))
                    is AppResult.Success -> _usernameState.postValue(true)
                }
            }
        }
    }
}