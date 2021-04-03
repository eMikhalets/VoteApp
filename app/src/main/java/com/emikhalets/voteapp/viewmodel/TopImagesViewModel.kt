package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopImagesViewModel @Inject constructor(
        private val databaseRepository: FirebaseDatabaseRepository,
) : ViewModel() {

    private val _images = MutableLiveData<List<Image>>()
    val images get():LiveData<List<Image>> = _images

    fun sendLoadTopImagesRequest() {
        if (_images.value.isNullOrEmpty()) {
            viewModelScope.launch {
                databaseRepository.loadTopImages {
                    if (it.isNotEmpty()) {
                        val images = it.sortedByDescending { item -> item.rating }
                        _images.postValue(images)
                    }
                }
            }
        }
    }
}