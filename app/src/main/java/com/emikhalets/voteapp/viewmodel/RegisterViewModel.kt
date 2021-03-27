package com.emikhalets.voteapp.viewmodel

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

class RegisterViewModel : ViewModel() {
    private val repository: AppRepository?
    private val disposables: CompositeDisposable
    private val throwable: MutableLiveData<String?>
    private val errorMessage: MutableLiveData<String?>
    private val liveDataLogin: MutableLiveData<Int>
    private val liveDataEmail: MutableLiveData<Int>
    private val liveDataRegister: MutableLiveData<Int>
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

    fun getLiveDataLogin(): LiveData<Int> {
        return liveDataLogin
    }

    fun getLiveDataEmail(): LiveData<Int> {
        return liveDataEmail
    }

    fun getLiveDataRegister(): LiveData<Int> {
        return liveDataRegister
    }

    fun register(email: String?, login: String?, password: String?, name: String?) {
        Timber.d("Send register request")
        val disposable = repository!!.registerRequest(email, login, password, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: ResponseBase -> onRegisterSuccess(response) }) { t: Throwable -> onError(t) }
        disposables.add(disposable)
    }

    fun checkEmail(email: String?) {
        Timber.d("Send check email request")
        val disposable = repository!!.checkEmailRequest(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: ResponseBase -> onCheckEmailSuccess(response) }) { t: Throwable -> onError(t) }
        disposables.add(disposable)
    }

    fun checkLogin(login: String?) {
        Timber.d("Send check login request")
        val disposable = repository!!.checkLoginRequest(login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: ResponseBase -> onCheckLoginSuccess(response) }) { t: Throwable -> onError(t) }
        disposables.add(disposable)
    }

    private fun onRegisterSuccess(response: ResponseBase) {
        val status = response.status
        Timber.d("Register request status %d", status)
        when (status) {
            200 -> liveDataRegister.setValue(status)
            500 -> errorMessage.setValue(response.errorMsg)
        }
    }

    private fun onCheckEmailSuccess(response: ResponseBase) {
        val status = response.status
        Timber.d("Check email request status %d", status)
        when (status) {
            200 -> errorMessage.setValue("Email занят")
            404 -> liveDataEmail.setValue(status)
        }
    }

    private fun onCheckLoginSuccess(response: ResponseBase) {
        val status = response.status
        Timber.d("Check login status %d", status)
        when (status) {
            200 -> errorMessage.setValue("Login занят")
            404 -> liveDataLogin.setValue(status)
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
        liveDataLogin = MutableLiveData()
        liveDataEmail = MutableLiveData()
        liveDataRegister = MutableLiveData()
    }
}