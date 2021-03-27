package com.emikhalets.voteapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emikhalets.voteapp.data.AppRepository
import com.emikhalets.voteapp.data.AppRepository.Companion.get
import com.emikhalets.voteapp.network.pojo.ResponseToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class LoginViewModel : ViewModel() {
    private val repository: AppRepository?
    private val disposables: CompositeDisposable
    private val throwable: MutableLiveData<String?>
    private val userToken: MutableLiveData<String?>
    private val errorMessage: MutableLiveData<String?>
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun getThrowable(): LiveData<String?> {
        return throwable
    }

    fun getUserToken(): LiveData<String?> {
        return userToken
    }

    fun getErrorMessage(): LiveData<String?> {
        return errorMessage
    }

    fun loginRequest(login: String?, password: String?) {
        Timber.d("Send login request")
        val disposable = repository!!.loginRequest(login, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: ResponseToken -> this.onSuccess(response) }) { t: Throwable -> onError(t) }
        disposables.add(disposable)
    }

    private fun onSuccess(response: ResponseToken) {
        val status = response.status
        Timber.d("Login request status %d", status)
        when (status) {
            200 -> userToken.setValue(response.data!!.userToken)
            500 -> errorMessage.setValue(response.errorMsg)
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
        userToken = MutableLiveData()
        errorMessage = MutableLiveData()
    }
}