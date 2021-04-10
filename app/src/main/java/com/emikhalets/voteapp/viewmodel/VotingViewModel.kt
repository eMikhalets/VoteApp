package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import com.emikhalets.voteapp.utils.AppResult
import com.emikhalets.voteapp.utils.Event
import com.emikhalets.voteapp.utils.ImageNumber
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class VotingViewModel @Inject constructor(
        private val databaseRepository: FirebaseDatabaseRepository
) : ViewModel() {

    private val _prepareState = MutableLiveData<Event<Boolean>>()
    val prepareState get(): LiveData<Event<Boolean>> = _prepareState

    private val _voteState = MutableLiveData<Boolean>()
    val voteState get(): LiveData<Boolean> = _voteState

    private val _image1 = MutableLiveData<Image>()
    val image1 get(): LiveData<Image> = _image1

    private val _image2 = MutableLiveData<Image>()
    val image2 get(): LiveData<Image> = _image2

    private val _error = MutableLiveData<Event<String>>()
    val error get(): LiveData<Event<String>> = _error

    private var images = emptyList<Image>()
    private var selectedName = ""

    fun sendPrepareVotingRequest() {
        viewModelScope.launch {
            databaseRepository.loadAllImages { result ->
                when (result) {
                    is AppResult.Error -> _error.postValue(Event(result.message))
                    is AppResult.Success -> {
                        images = result.data
                        setNextRandomImages()
                        _prepareState.postValue(Event(true))
                    }
                }
            }
        }
    }

    fun setSelectedImage(image: ImageNumber) {
        selectedName = when (image) {
            ImageNumber.FIRST -> _image1.value?.name ?: ""
            ImageNumber.SECOND -> _image2.value?.name ?: ""
        }
    }

    fun sendVoteRequest() {
        if (selectedName.isNotEmpty()) {
            viewModelScope.launch {
                databaseRepository.updateImageRating(selectedName) { result ->
                    when (result) {
                        is AppResult.Error -> _error.postValue(Event(result.message))
                        is AppResult.Success -> {
                            selectedName = ""
                            setNextRandomImages()
                            _voteState.postValue(true)
                        }
                    }
                }
            }
        } else _error.postValue(Event("Need to choose a image"))
    }

    private fun setNextRandomImages() {
        viewModelScope.launch {
            if (images.isNotEmpty()) {
                val first = Random.nextInt(0, images.size)
                var second = Random.nextInt(0, images.size)
                if (first == second) second = (second + 1) % images.size
                _image1.postValue(images[first])
                _image2.postValue(images[second])
            }
        }
    }
}