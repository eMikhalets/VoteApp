package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import com.emikhalets.voteapp.utils.ImageNumber
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class VotingViewModel @Inject constructor(
        private val databaseRepository: FirebaseDatabaseRepository,
) : ViewModel() {

    private val _image1 = MutableLiveData<Image>()
    val image1 get():LiveData<Image> = _image1

    private val _image2 = MutableLiveData<Image>()
    val image2 get():LiveData<Image> = _image2

    private var images = emptyList<Image>()
    private var selectedName = ""

    fun sendPrepareVotingRequest(onComplete: () -> Unit) {
        if (images.isEmpty()) {
            viewModelScope.launch {
                databaseRepository.loadAllImages { list ->
                    images = list
                    setNextRandomImages()
                    onComplete()
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

    fun sendVoteRequest(onComplete: () -> Unit) {
        if (selectedName.isNotEmpty()) {
            viewModelScope.launch {
                databaseRepository.updateImageRating(selectedName) { user_id ->
                    databaseRepository.updateUserRating(user_id) {
                        selectedName = ""
                        setNextRandomImages()
                        onComplete()
                    }
                }
            }
        } else onComplete()
    }

    private fun setNextRandomImages() {
        if (images.isNotEmpty()) {
            val first = Random.nextInt(0, images.size)
            var second = Random.nextInt(0, images.size)
            if (first == second) second = (second + 1) % images.size
            _image1.postValue(images[first])
            _image2.postValue(images[second])
        }
    }
}