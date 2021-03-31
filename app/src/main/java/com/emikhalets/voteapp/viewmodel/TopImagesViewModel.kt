package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.utils.DATABASE_REPOSITORY
import kotlinx.coroutines.launch

class TopImagesViewModel : ViewModel() {

    private val _images = MutableLiveData<List<Image>>()
    val images get():LiveData<List<Image>> = _images

    fun sendLoadTopImages() {
        if (_images.value.isNullOrEmpty()) {
            viewModelScope.launch {
                DATABASE_REPOSITORY.loadTopImages {
                    if (it.isNotEmpty()) {
                        val images = it.sortedByDescending { item -> item.rating }
                        _images.postValue(images)
                    }
                }
            }
        }
    }
}