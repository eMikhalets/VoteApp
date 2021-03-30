package com.emikhalets.voteapp.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.utils.DATABASE_REPOSITORY
import com.emikhalets.voteapp.utils.STORAGE_REPOSITORY
import com.emikhalets.voteapp.viewmodel.UserImagesViewModel.SortState.*
import kotlinx.coroutines.launch

class UserImagesViewModel : ViewModel() {

    private var images = mutableListOf<Image>()
    private var sortState = DATE_DEC
    private var isSortedByDate = true

    fun sendLoadUserImagesRequest(onComplete: (List<Image>) -> Unit) {
        viewModelScope.launch {
            DATABASE_REPOSITORY.loadUserImages {
                if (it.isNotEmpty()) {
                    images = it.toMutableList()
                    sortImagesByDateWhenAddImage()
                    onComplete(images)
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
                                images.add(image)
                                if (isSortedByDate) sortImagesByDateWhenAddImage()
                                else sortImagesByRatingWhenAddImage()
                                onSuccess(images)
                            }
                        }
                    }
                }
            }
        }
    }

    // TODO как вынести определение состояния сортировки в отдельную функцию?
    fun sortImagesByDate(onComplete: (List<Image>) -> Unit) {
        viewModelScope.launch {
            when (sortState) {
                RATING_DEC -> {
                    images = images.sortedByDescending { it.timestamp }.toMutableList()
                    sortState = DATE_DEC
                }
                RATING_ASC -> {
                    images = images.sortedByDescending { it.timestamp }.toMutableList()
                    sortState = DATE_DEC
                }
                DATE_DEC -> {
                    images = images.sortedBy { it.timestamp }.toMutableList()
                    sortState = DATE_ASC
                }
                DATE_ASC -> {
                    images = images.sortedByDescending { it.timestamp }.toMutableList()
                    sortState = DATE_DEC
                }
            }
            isSortedByDate = true
            onComplete(images)
        }
    }

    fun sortImagesByRating(onComplete: (List<Image>) -> Unit) {
        viewModelScope.launch {
            when (sortState) {
                RATING_DEC -> {
                    images = images.sortedBy { it.rating }.toMutableList()
                    sortState = RATING_ASC
                }
                RATING_ASC -> {
                    images = images.sortedByDescending { it.rating }.toMutableList()
                    sortState = RATING_DEC
                }
                DATE_DEC -> {
                    images = images.sortedByDescending { it.rating }.toMutableList()
                    sortState = RATING_DEC
                }
                DATE_ASC -> {
                    images = images.sortedByDescending { it.rating }.toMutableList()
                    sortState = RATING_DEC
                }
            }
            isSortedByDate = false
            onComplete(images)
        }
    }

    private fun sortImagesByDateWhenAddImage() {
        if (images.size > 1) {
            when (sortState) {
                DATE_DEC -> images = images.sortedByDescending { it.timestamp }.toMutableList()
                DATE_ASC -> images = images.sortedBy { it.timestamp }.toMutableList()
                else -> {
                }
            }
        }
    }

    private fun sortImagesByRatingWhenAddImage() {
        if (images.size > 1) {
            when (sortState) {
                RATING_DEC -> images = images.sortedByDescending { it.rating }.toMutableList()
                RATING_ASC -> images = images.sortedBy { it.rating }.toMutableList()
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