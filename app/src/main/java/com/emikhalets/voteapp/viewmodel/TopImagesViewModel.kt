package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import com.emikhalets.voteapp.utils.AppResult
import com.emikhalets.voteapp.utils.Event
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopImagesViewModel @Inject constructor(
        private val databaseRepository: FirebaseDatabaseRepository,
) : ViewModel() {

    private val _images = MutableLiveData<List<Image>>()
    val images get():LiveData<List<Image>> = _images

    private val _error = MutableLiveData<Event<String>>()
    val error get(): LiveData<Event<String>> = _error

    fun sendLoadTopImagesRequest() {
        if (_images.value == null) {
            viewModelScope.launch {
                databaseRepository.loadTopImages { result ->
                    when (result) {
                        is AppResult.Success -> {
                            val list = result.data.sortedByDescending { it.rating }
                            _images.postValue(list)
                        }
                        is AppResult.Error -> _error.postValue(Event(result.message))
                    }
                }
            }
        }
    }
}