package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
        private val databaseRepository: FirebaseDatabaseRepository,
) : ViewModel() {

    private val _images = MutableLiveData<List<Image>>()
    val images get():LiveData<List<Image>> = _images

    fun sendLatestImagesRequest(isRefresh: Boolean = false) {
        if (_images.value == null || isRefresh) {
            viewModelScope.launch {
                databaseRepository.loadLatestImages { images ->
                    val list = images.sortedByDescending { it.timestamp }
                    _images.postValue(list)
                }
            }
        }
    }
}