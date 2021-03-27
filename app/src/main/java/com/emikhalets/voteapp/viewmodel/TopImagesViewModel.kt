package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emikhalets.voteapp.data.AppRepository
import com.emikhalets.voteapp.data.AppRepository.Companion.get
import com.emikhalets.voteapp.network.pojo.DataImage
import com.emikhalets.voteapp.network.pojo.ResponseImages
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class TopImagesViewModel : ViewModel() {
    private val repository: AppRepository?
    private val disposables: CompositeDisposable
    private val throwable: MutableLiveData<String?>
    private val errorMessage: MutableLiveData<String?>
    private val images: MutableLiveData<List<DataImage>?>
    private var userToken: String? = ""
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun getThrowable(): LiveData<String?> {
        return throwable
    }

    fun getErrorMessage(): LiveData<String?> {
        return errorMessage
    }

    fun getImages(): LiveData<List<DataImage>?> {
        return images
    }

    fun setUserToken(userToken: String?) {
        this.userToken = userToken
    }

    fun topPhotosRequest() {
        Timber.d("Send top photos request")
        val disposable = repository!!.topPhotosRequest(userToken, "10", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: ResponseImages -> this.onSuccess(response) }) { t: Throwable -> onError(t) }
        disposables.add(disposable)
    }

    private fun onSuccess(response: ResponseImages) {
        val status = response.status
        Timber.d("Top photos request status %d", status)
        when (status) {
            200 -> images.setValue(response.data)
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
        throwable = MutableLiveData()
        errorMessage = MutableLiveData()
        images = MutableLiveData()
    }
}