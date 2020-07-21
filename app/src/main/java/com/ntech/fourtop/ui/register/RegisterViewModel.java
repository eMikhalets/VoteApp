package com.ntech.fourtop.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntech.fourtop.data.AppRepository;
import com.ntech.fourtop.network.pojo.ResponseBase;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RegisterViewModel extends ViewModel {

    private AppRepository repository;
    private CompositeDisposable disposables;
    private MutableLiveData<String> throwable;
    private MutableLiveData<String> errorMessage;
    private MutableLiveData<Integer> liveDataLogin;
    private MutableLiveData<Integer> liveDataEmail;
    private MutableLiveData<Integer> liveDataRegister;

    public RegisterViewModel() {
        repository = AppRepository.get();
        disposables = new CompositeDisposable();
        throwable = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        liveDataLogin = new MutableLiveData<>();
        liveDataEmail = new MutableLiveData<>();
        liveDataRegister = new MutableLiveData<>();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public LiveData<String> getThrowable() {
        return throwable;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Integer> getLiveDataLogin() {
        return liveDataLogin;
    }

    public LiveData<Integer> getLiveDataEmail() {
        return liveDataEmail;
    }

    public LiveData<Integer> getLiveDataRegister() {
        return liveDataRegister;
    }

    public void register(String email, String login, String password, String name) {
        Timber.d("Send register request");
        Disposable disposable = repository.registerRequest(email, login, password, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onRegisterSuccess, this::onError);
        disposables.add(disposable);
    }

    public void checkEmail(String email) {
        Timber.d("Send check email request");
        Disposable disposable = repository.checkEmailRequest(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCheckEmailSuccess, this::onError);
        disposables.add(disposable);
    }

    public void checkLogin(String login) {
        Timber.d("Send check login request");
        Disposable disposable = repository.checkLoginRequest(login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCheckLoginSuccess, this::onError);
        disposables.add(disposable);
    }

    private void onRegisterSuccess(ResponseBase response) {
        int status = response.getStatus();
        Timber.d("Register request status %d", status);

        switch (status) {
            case 200:
                liveDataRegister.setValue(status);
                break;
            case 500:
                errorMessage.setValue(response.getErrorMsg());
                break;
        }
    }

    private void onCheckEmailSuccess(ResponseBase response) {
        int status = response.getStatus();
        Timber.d("Check email request status %d", status);

        switch (status) {
            case 200:
                liveDataEmail.setValue(status);
                break;
            case 404:
                liveDataEmail.setValue(status);
                break;
        }
    }

    private void onCheckLoginSuccess(ResponseBase response) {
        int status = response.getStatus();
        Timber.d("Check login status %d", status);

        switch (status) {
            case 200:
                liveDataLogin.setValue(status);
                break;
            case 404:
                liveDataLogin.setValue(status);
                break;
        }
    }

    private void onError(Throwable t) {
        Timber.d(t);
        throwable.setValue(t.toString());
    }

    public void checkPassMatch() {
//        String passStr = password.getValue();
//        String confPassStr = confPass.getValue();
//
//        if (passStr != null && !passStr.isEmpty() && confPassStr != null && !confPassStr.isEmpty()
//                && passStr.equals(confPassStr)) {
//            passIsMatched = true;
//        } else {
//            passIsMatched = false;
//        }
    }
}
