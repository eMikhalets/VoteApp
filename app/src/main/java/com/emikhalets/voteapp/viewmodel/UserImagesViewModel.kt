package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import com.emikhalets.voteapp.model.firebase.FirebaseStorageRepository
import com.emikhalets.voteapp.utils.AppResult
import com.emikhalets.voteapp.utils.AppValueEventListener
import com.emikhalets.voteapp.utils.Event
import com.emikhalets.voteapp.utils.toImage
import com.emikhalets.voteapp.viewmodel.UserImagesViewModel.SortState.*
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class UserImagesViewModel @Inject constructor(
        private val databaseRepository: FirebaseDatabaseRepository,
        private val storageRepository: FirebaseStorageRepository
) : ViewModel() {

    private val _images = MutableLiveData<List<Image>>()
    val images get(): LiveData<List<Image>> = _images

    private val _saveImageState = MutableLiveData<Boolean>()
    val saveImageState get(): LiveData<Boolean> = _saveImageState

    private val _deleteState = MutableLiveData<Boolean>()
    val deleteState get(): LiveData<Boolean> = _deleteState

    private val _error = MutableLiveData<Event<String>>()
    val error get(): LiveData<Event<String>> = _error

    private var imageSortingState = DATE_DEC

    // Требуется инициализация этих преременных, так как в диалоге используется эта же вьюмодель,
    // а при выходе из диалога идет обращение в onCleared к неинициализированной userReference,
    // и делать отдельные вьюмодели для кажного диалога мне лень
    private val userReference: Query = databaseRepository.listenUserImagesChanges()
    private val userDataListener: ValueEventListener = AppValueEventListener {
        viewModelScope.launch {
            var imagesList = mutableListOf<Image>()
            it.children.forEach { item -> imagesList.add(item.toImage()) }
            imagesList = sortImagesWhenAdding(imagesList).toMutableList()
            _images.postValue(imagesList)
        }
    }

    override fun onCleared() {
        super.onCleared()
        userReference.removeEventListener(userDataListener)
    }

    fun sendLoadUserImagesRequest() {
        if (_images.value.isNullOrEmpty()) {
            viewModelScope.launch {
                userReference.addValueEventListener(userDataListener)
            }
        }
    }

    fun sendSaveImageRequest(uri: String) {
        viewModelScope.launch {
            if (uri.isNotEmpty()) saveImageInStorage(uri)
            else _error.postValue(Event("Image Uri is empty"))
        }
    }

    private fun saveImageInStorage(uri: String) {
        viewModelScope.launch {
            val imageName = UUID.randomUUID().toString()
            storageRepository.saveImage(imageName, uri) { result ->
                when (result) {
                    is AppResult.Success -> saveImageInDatabase(imageName, result.data)
                    is AppResult.Error -> _error.postValue(Event(result.message))
                }
            }
        }
    }

    private fun saveImageInDatabase(name: String, url: String) {
        viewModelScope.launch {
            databaseRepository.saveUserImage(name, url) { result ->
                when (result) {
                    is AppResult.Success -> _saveImageState.postValue(true)
                    is AppResult.Error -> _error.postValue(Event(result.message))
                }
            }
        }
    }

    fun sendDeleteImageRequest(name: String) {
        viewModelScope.launch {
            storageRepository.deleteImage(name) { result ->
                when (result) {
                    is AppResult.Success -> deleteImageInDatabase(name)
                    is AppResult.Error -> _error.postValue(Event(result.message))
                }
            }
        }
    }

    private fun deleteImageInDatabase(name: String) {
        viewModelScope.launch {
            databaseRepository.deleteImage(name) { result ->
                when (result) {
                    is AppResult.Success -> _deleteState.postValue(true)
                    is AppResult.Error -> _error.postValue(Event(result.message))
                }
            }
        }
    }

    fun sortImagesByDate() {
        viewModelScope.launch {
            _images.value?.let { list ->
                val imagesList = when (imageSortingState) {
                    DATE_DEC -> {
                        imageSortingState = DATE_ASC
                        list.sortedBy { it.timestamp }.toMutableList()
                    }
                    else -> {
                        imageSortingState = DATE_DEC
                        list.sortedByDescending { it.timestamp }.toMutableList()
                    }
                }
                _images.postValue(imagesList)
            }
        }
    }

    fun sortImagesByRating() {
        viewModelScope.launch {
            _images.value?.let { list ->
                val imagesList = when (imageSortingState) {
                    RATING_DEC -> {
                        imageSortingState = RATING_ASC
                        list.sortedBy { it.rating }.toMutableList()
                    }
                    else -> {
                        imageSortingState = RATING_DEC
                        list.sortedByDescending { it.rating }.toMutableList()
                    }
                }
                _images.postValue(imagesList)
            }
        }
    }

    private suspend fun sortImagesWhenAdding(list: List<Image>): List<Image> =
            withContext(Dispatchers.IO) {
                when (imageSortingState) {
                    DATE_DEC -> list.sortedByDescending { it.timestamp }.toMutableList()
                    DATE_ASC -> list.sortedBy { it.timestamp }.toMutableList()
                    RATING_DEC -> list.sortedByDescending { it.rating }.toMutableList()
                    RATING_ASC -> list.sortedBy { it.rating }.toMutableList()
                }
            }

    private enum class SortState {
        RATING_DEC,
        RATING_ASC,
        DATE_DEC,
        DATE_ASC;
    }
}