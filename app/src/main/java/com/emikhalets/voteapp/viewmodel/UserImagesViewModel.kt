package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emikhalets.voteapp.data.AppRepository
import com.emikhalets.voteapp.data.AppRepository.Companion.get
import com.emikhalets.voteapp.network.pojo.DataImage
import com.emikhalets.voteapp.network.pojo.DataProfile
import com.emikhalets.voteapp.network.pojo.ResponseBase
import com.emikhalets.voteapp.network.pojo.ResponseImages
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.File

class UserImagesViewModel : ViewModel() {
    private val repository: AppRepository?
    private val disposables: CompositeDisposable
    private val images: MutableLiveData<List<DataImage>?>
    private val throwable: MutableLiveData<String?>
    private val profile: MutableLiveData<DataProfile>
    private val errorMessage: MutableLiveData<String?>
    private var userToken: String? = ""
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun getImages(): LiveData<List<DataImage>?> {
        return images
    }

    fun getThrowable(): LiveData<String?> {
        return throwable
    }

    fun getProfile(): LiveData<DataProfile> {
        return profile
    }

    fun getErrorMessage(): LiveData<String?> {
        return errorMessage
    }

    fun setUserToken(userToken: String?) {
        this.userToken = userToken
    }

    fun galleryRequest() {
        Timber.d("Send gallery request")
        val disposable = repository!!.galleryRequest(userToken, "10", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: ResponseImages -> this.onSuccess(response) }) { t: Throwable -> onError(t) }
        disposables.add(disposable)
    }

    fun galleryAddRequest(file: File?) {
        Timber.d("Send gallery add request")
        val disposable = repository!!.galleryAddRequest(userToken, file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: ResponseBase -> this.onSuccess(response) }) { t: Throwable -> onError(t) }
        disposables.add(disposable)
    }

    fun galleryRemoveRequest(position: Int) {
        if (images.value != null) {
            val id = images.value!![position].toString()
            Timber.d("Send gallery remove request")
            val disposable = repository!!.galleryRemoveRequest(userToken, id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response: ResponseBase -> this.onSuccess(response) }) { t: Throwable -> onError(t) }
            disposables.add(disposable)
        }
    }

    private fun onSuccess(response: ResponseImages) {
        val status = response.status
        Timber.d("Gallery request status %d", status)
        when (status) {
            200 -> images.setValue(response.data)
            500 -> errorMessage.setValue(response.errorMsg)
        }
    }

    private fun onSuccess(response: ResponseBase) {
        val status = response.status
        Timber.d("Gallery change request status %d", status)
        when (status) {
            200 -> galleryRequest()
            500 -> errorMessage.setValue(response.errorMsg)
        }
    }

    private fun onError(t: Throwable) {
        Timber.d(t)
        throwable.value = t.toString()
    }

    init {
        repository = get()
        disposables = CompositeDisposable()
        images = MutableLiveData()
        throwable = MutableLiveData()
        profile = MutableLiveData()
        errorMessage = MutableLiveData()
    }
}