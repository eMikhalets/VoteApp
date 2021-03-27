package com.emikhalets.voteapp.view.logo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emikhalets.voteapp.data.AppRepository
import com.emikhalets.voteapp.data.AppRepository.Companion.get
import com.emikhalets.voteapp.network.pojo.ResponseBase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class LogoViewModel : ViewModel() {
    private val repository: AppRepository?
    private val disposables: CompositeDisposable
    private val throwable: MutableLiveData<String?>
    private val errorMessage: MutableLiveData<String?>
    private val liveDataResponse: MutableLiveData<Int>
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

    fun getLiveDataResponse(): LiveData<Int> {
        return liveDataResponse
    }

    fun tokenRequest(token: String?) {
        Timber.d("Send token request")
        val disposable = repository!!.tokenRequest(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: ResponseBase -> this.onSuccess(response) }) { t: Throwable -> onError(t) }
        disposables.add(disposable)
    }

    private fun onSuccess(response: ResponseBase) {
        val status = response.status
        Timber.d("Check token request status %d", status)
        when (status) {
            200 -> liveDataResponse.setValue(status)
            403 -> errorMessage.setValue(response.errorMsg)
        }
    }

    private fun onError(t: Throwable) {
        Timber.d(t)
        throwable.value = t.message
    }

    init {
        repository = get()
        disposables = CompositeDisposable()
        throwable = MutableLiveData()
        errorMessage = MutableLiveData()
        liveDataResponse = MutableLiveData()
    }
}