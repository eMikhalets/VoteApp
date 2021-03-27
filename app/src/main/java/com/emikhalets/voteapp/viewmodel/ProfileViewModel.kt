package com.emikhalets.voteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emikhalets.voteapp.data.AppRepository
import com.emikhalets.voteapp.data.AppRepository.Companion.get
import com.emikhalets.voteapp.network.pojo.DataProfile
import com.emikhalets.voteapp.network.pojo.ResponseBase
import com.emikhalets.voteapp.network.pojo.ResponseProfile
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ProfileViewModel : ViewModel() {
    private val repository: AppRepository?
    private val disposables: CompositeDisposable
    private val logout: MutableLiveData<Int>
    private val throwable: MutableLiveData<String?>
    private val profile: MutableLiveData<DataProfile?>
    private val errorMessage: MutableLiveData<String?>
    private var userToken: String? = ""
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun getLogout(): LiveData<Int> {
        return logout
    }

    fun getThrowable(): LiveData<String?> {
        return throwable
    }

    fun getProfile(): LiveData<DataProfile?> {
        return profile
    }

    fun getErrorMessage(): LiveData<String?> {
        return errorMessage
    }

    fun setUserToken(userToken: String?) {
        this.userToken = userToken
    }

    fun profileRequest() {
        Timber.d("Send profile request")
        val disposable = repository!!.profileRequest(userToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: ResponseProfile -> this.onSuccess(response) }) { t: Throwable -> onError(t) }
        disposables.add(disposable)
    }

    fun logoutRequest() {
        Timber.d("Send logout request")
        val disposable = repository!!.logoutRequest(userToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: ResponseBase -> this.onSuccess(response) }) { t: Throwable -> onError(t) }
        disposables.add(disposable)
    }

    private fun onSuccess(response: ResponseProfile) {
        val status = response.status
        Timber.d("Profile request status %d", status)
        when (status) {
            200 -> profile.setValue(response.data)
            500 -> errorMessage.setValue(response.errorMsg)
        }
    }

    private fun onSuccess(response: ResponseBase) {
        val status = response.status
        Timber.d("Logout request status %d", status)
        when (status) {
            200 -> logout.setValue(status)
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
        logout = MutableLiveData()
        throwable = MutableLiveData()
        profile = MutableLiveData()
        errorMessage = MutableLiveData()
    }
}