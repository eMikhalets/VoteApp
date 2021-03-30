package com.emikhalets.voteapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.utils.DATABASE_REPOSITORY
import com.emikhalets.voteapp.utils.STORAGE_REPOSITORY
import com.emikhalets.voteapp.viewmodel.UserImagesViewModel.SortState.*
import kotlinx.coroutines.launch

class UserImagesViewModel : ViewModel() {

    private val _images = MutableLiveData<List<Image>>()
    val images get():LiveData<List<Image>> = _images

    private var imagesList = mutableListOf<Image>()
    private var sortState = DATE_DEC
    private var isSortedByDate = true

    fun sendLoadUserImagesRequest(onComplete: (List<Image>) -> Unit) {
        if (_images.value.isNullOrEmpty()) {
            viewModelScope.launch {
                DATABASE_REPOSITORY.loadUserImages {
                    if (it.isNotEmpty()) {
                        imagesList = it.toMutableList()
                        sortImagesByDateWhenAddImage()
                        _images.postValue(imagesList)
                        onComplete(imagesList)
                    }
                }
            }
        }
    }

    fun sendSaveImageRequest(uri: Uri?, onSuccess: (List<Image>) -> Unit) {
        uri?.let {
            viewModelScope.launch {
                STORAGE_REPOSITORY.saveImage(it) { imageName ->
                    STORAGE_REPOSITORY.loadImageUrl(imageName) { url ->
                        DATABASE_REPOSITORY.saveImage(imageName, url) {
                            DATABASE_REPOSITORY.loadAddedUserImage(imageName) { image ->
                                imagesList.add(image)
                                if (isSortedByDate) sortImagesByDateWhenAddImage()
                                else sortImagesByRatingWhenAddImage()
                                _images.postValue(imagesList)
                                onSuccess(imagesList)
                            }
                        }
                    }
                }
            }
        }
    }

    // TODO как вынести определение состояния сортировки в отдельную функцию?
    fun sortImagesByDate() {
        viewModelScope.launch {
            imagesList = when (sortState) {
                RATING_DEC -> {
                    sortState = DATE_DEC
                    imagesList.sortedByDescending { it.timestamp }.toMutableList()
                }
                RATING_ASC -> {
                    sortState = DATE_DEC
                    imagesList.sortedByDescending { it.timestamp }.toMutableList()
                }
                DATE_DEC -> {
                    sortState = DATE_ASC
                    imagesList.sortedBy { it.timestamp }.toMutableList()
                }
                DATE_ASC -> {
                    sortState = DATE_DEC
                    imagesList.sortedByDescending { it.timestamp }.toMutableList()
                }
            }
            isSortedByDate = true
            _images.postValue(imagesList)
        }
    }

    fun sortImagesByRating() {
        viewModelScope.launch {
            imagesList = when (sortState) {
                RATING_DEC -> {
                    sortState = RATING_ASC
                    imagesList.sortedBy { it.rating }.toMutableList()
                }
                RATING_ASC -> {
                    sortState = RATING_DEC
                    imagesList.sortedByDescending { it.rating }.toMutableList()
                }
                DATE_DEC -> {
                    sortState = RATING_DEC
                    imagesList.sortedByDescending { it.rating }.toMutableList()
                }
                DATE_ASC -> {
                    sortState = RATING_DEC
                    imagesList.sortedByDescending { it.rating }.toMutableList()
                }
            }
            isSortedByDate = false
            _images.postValue(imagesList)
        }
    }

    private fun sortImagesByDateWhenAddImage() {
        if (imagesList.size > 1) {
            when (sortState) {
                DATE_DEC -> imagesList = imagesList.sortedByDescending { it.timestamp }.toMutableList()
                DATE_ASC -> imagesList = imagesList.sortedBy { it.timestamp }.toMutableList()
                else -> {
                }
            }
        }
    }

    private fun sortImagesByRatingWhenAddImage() {
        if (imagesList.size > 1) {
            when (sortState) {
                RATING_DEC -> imagesList = imagesList.sortedByDescending { it.rating }.toMutableList()
                RATING_ASC -> imagesList = imagesList.sortedBy { it.rating }.toMutableList()
                else -> {
                }
            }
        }
    }

    private enum class SortState {
        RATING_DEC,
        RATING_ASC,
        DATE_DEC,
        DATE_ASC;
    }
}