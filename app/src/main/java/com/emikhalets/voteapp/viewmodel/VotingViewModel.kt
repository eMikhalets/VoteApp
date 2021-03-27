package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emikhalets.voteapp.data.AppRepository
import com.emikhalets.voteapp.data.AppRepository.Companion.get
import com.emikhalets.voteapp.network.pojo.ResponseBase
import com.emikhalets.voteapp.network.pojo.ResponseVoting
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class VotingViewModel : ViewModel() {
    private val repository: AppRepository?
    private val disposables: CompositeDisposable
    private val throwable: MutableLiveData<String?>
    private val voting: MutableLiveData<Int>
    private val errorMessage: MutableLiveData<String?>
    private var userToken: String? = ""
    private var voteToken: String? = ""
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun getThrowable(): LiveData<String?> {
        return throwable
    }

    fun getVoting(): LiveData<Int> {
        return voting
    }

    fun getErrorMessage(): LiveData<String?> {
        return errorMessage
    }

    fun setUserToken(userToken: String?) {
        this.userToken = userToken
    }

    fun voteCreateRequest() {
        Timber.d("Send vote create request")
        val disposable = repository!!.voteCreateRequest(userToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: ResponseVoting -> this.onSuccess(response) }) { t: Throwable -> onError(t) }
        disposables.add(disposable)
    }

    fun voteRequest(voteValue: Int) {
        val vote = voteValue.toString()
        Timber.d("Send vote request")
        val disposable = repository!!.voteRequest(userToken, voteToken, vote)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: ResponseBase -> this.onSuccess(response) }) { t: Throwable -> onError(t) }
        disposables.add(disposable)
    }

    private fun onSuccess(response: ResponseVoting) {
        val status = response.status
        Timber.d("Vote create request status %d", status)
        when (status) {
            200 -> voteToken = response.data!!.voteToken
            500 -> errorMessage.setValue(response.errorMsg)
        }
    }

    private fun onSuccess(response: ResponseBase) {
        val status = response.status
        Timber.d("Logout request status %d", status)
        when (status) {
            200 -> voting.setValue(status)
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
        voting = MutableLiveData()
        errorMessage = MutableLiveData()
    }
}