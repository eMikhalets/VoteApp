package com.emikhalets.voteapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import com.emikhalets.voteapp.model.firebase.FirebaseStorageRepository
import com.emikhalets.voteapp.viewmodel.UserImagesViewModel.SortState.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserImagesViewModel @Inject constructor(
        private val databaseRepository: FirebaseDatabaseRepository,
        private val storageRepository: FirebaseStorageRepository,
) : ViewModel() {

    private val _images = MutableLiveData<List<Image>>()
    val images get():LiveData<List<Image>> = _images

    private var imagesList = mutableListOf<Image>()
    private var sortState = DATE_DEC

    fun sendLoadUserImagesRequest() {
        if (_images.value.isNullOrEmpty()) {
            viewModelScope.launch {
                databaseRepository.loadUserImages {
                    if (it.isNotEmpty()) {
                        imagesList = it.toMutableList()
                        sortImagesWhenAdding()
                        _images.postValue(imagesList)
                    }
                }
            }
        }
    }

    fun sendSaveImageRequest(uri: Uri?, onSuccess: (List<Image>) -> Unit) {
        uri?.let {
            viewModelScope.launch {
                storageRepository.saveImage(it) { name, url ->
                    databaseRepository.saveUserImage(name, url) { image ->
                        imagesList.add(image)
                        sortImagesWhenAdding()
                        _images.postValue(imagesList)
                        onSuccess(imagesList)
                    }
                }
            }
        }
    }

    fun sendDeleteImageRequest(name: String, position: Int, onSuccess: () -> Unit) {
        if (name.isEmpty()) onSuccess()
        else viewModelScope.launch {
            storageRepository.deleteImage(name) {
                databaseRepository.deleteImage(name) {
                    imagesList.removeAt(position)
                    _images.postValue(imagesList)
                    onSuccess()
                }
            }
        }
    }

    fun sortImagesByDate() {
        viewModelScope.launch {
            imagesList = if (sortState == DATE_DEC) {
                sortState = DATE_ASC
                imagesList.sortedBy { it.timestamp }.toMutableList()
            } else {
                sortState = DATE_DEC
                imagesList.sortedByDescending { it.timestamp }.toMutableList()
            }
            _images.postValue(imagesList)
        }
    }

    fun sortImagesByRating() {
        viewModelScope.launch {
            imagesList = if (sortState == RATING_DEC) {
                sortState = RATING_ASC
                imagesList.sortedBy { it.rating }.toMutableList()
            } else {
                sortState = RATING_DEC
                imagesList.sortedByDescending { it.rating }.toMutableList()
            }
            _images.postValue(imagesList)
        }
    }

    private fun sortImagesWhenAdding() {
        imagesList = when (sortState) {
            DATE_DEC -> imagesList.sortedByDescending { it.timestamp }.toMutableList()
            DATE_ASC -> imagesList.sortedBy { it.timestamp }.toMutableList()
            RATING_DEC -> imagesList.sortedByDescending { it.rating }.toMutableList()
            RATING_ASC -> imagesList.sortedBy { it.rating }.toMutableList()
        }
    }

    private enum class SortState {
        RATING_DEC,
        RATING_ASC,
        DATE_DEC,
        DATE_ASC;
    }
}