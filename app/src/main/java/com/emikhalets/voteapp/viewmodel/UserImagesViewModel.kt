package com.emikhalets.voteapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import com.emikhalets.voteapp.model.firebase.FirebaseStorageRepository
import com.emikhalets.voteapp.utils.AppValueEventListener
import com.emikhalets.voteapp.utils.toImage
import com.emikhalets.voteapp.viewmodel.UserImagesViewModel.SortState.*
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserImagesViewModel @Inject constructor(
        private val databaseRepository: FirebaseDatabaseRepository,
        private val storageRepository: FirebaseStorageRepository,
) : ViewModel() {

    private val _images = MutableLiveData<List<Image>>()
    val images get():LiveData<List<Image>> = _images

    private val userReference: Query = databaseRepository.listenUserImagesChanges()
    private var imageSortingState = DATE_DEC

    private val userDataListener: ValueEventListener = AppValueEventListener {
        var imagesList = mutableListOf<Image>()
        it.children.forEach { item -> imagesList.add(item.toImage()) }
        imagesList = sortImagesWhenAdding(imagesList).toMutableList()
        _images.postValue(imagesList)
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

    fun sendSaveImageRequest(uri: Uri?, onComplete: (success: Boolean, error: String) -> Unit) {
        uri?.let {
            viewModelScope.launch {
                storageRepository.saveImage(it) { isSuccess, name, url, saveError ->
                    if (isSuccess) {
                        suspend {
                            databaseRepository.saveUserImage(name, url) { image, error ->
                                if (image != null) onComplete(true, "")
                                else onComplete(false, error)
                            }
                        }
                    } else {
                        onComplete(false, saveError)
                    }
                }
            }
        }
    }

    fun sendDeleteImageRequest(name: String, onComplete: (success: Boolean, error: String) -> Unit) {
        if (name.isEmpty()) onComplete(false, "Image name is empty")
        else viewModelScope.launch {
            storageRepository.deleteImage(name) { isStorageSuccess, storageError ->
                if (isStorageSuccess) {
                    suspend {
                        databaseRepository.deleteImage(name) { isSuccess, error ->
                            if (isSuccess) onComplete(true, "")
                            else onComplete(false, error)
                        }
                    }
                } else {
                    onComplete(false, storageError)
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

    private fun sortImagesWhenAdding(list: List<Image>): List<Image> {
        return when (imageSortingState) {
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