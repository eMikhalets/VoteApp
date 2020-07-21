package com.ntech.fourtop.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntech.fourtop.data.AppRepository;
import com.ntech.fourtop.network.pojo.ResponseToken;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class LoginViewModel extends ViewModel {

    private AppRepository repository;
    private CompositeDisposable disposables;
    private MutableLiveData<String> throwable;
    private MutableLiveData<String> userToken;
    private MutableLiveData<String> errorMessage;

    public LoginViewModel() {
        repository = AppRepository.get();
        disposables = new CompositeDisposable();
        throwable = new MutableLiveData<>();
        userToken = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public LiveData<String> getThrowable() {
        return throwable;
    }

    public LiveData<String> getUserToken() {
        return userToken;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loginRequest(String login, String password) {
        Timber.d("Send login request");
        Disposable disposable = repository.loginRequest(login, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError);
        disposables.add(disposable);
    }

    private void onSuccess(ResponseToken response) {
        int status = response.getStatus();
        Timber.d("Login request status %d", status);

        switch (status) {
            case 200:
                userToken.setValue(response.getData().getUserToken());
                break;
            case 500:
                errorMessage.setValue(response.getErrorMsg());
                break;
        }
    }

    private void onError(Throwable t) {
        Timber.d(t);
        throwable.setValue(t.toString());
    }
}
