package com.ntech.fourtop.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntech.fourtop.data.AppRepository;
import com.ntech.fourtop.network.pojo.DataProfile;
import com.ntech.fourtop.network.pojo.ResponseBase;
import com.ntech.fourtop.network.pojo.ResponseProfile;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ProfileViewModel extends ViewModel {

    private AppRepository repository;
    private CompositeDisposable disposables;
    private MutableLiveData<Integer> logout;
    private MutableLiveData<String> throwable;
    private MutableLiveData<DataProfile> profile;
    private MutableLiveData<String> errorMessage;
    private String userToken = "";

    public ProfileViewModel() {
        repository = AppRepository.get();
        disposables = new CompositeDisposable();
        logout = new MutableLiveData<>();
        throwable = new MutableLiveData<>();
        profile = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public LiveData<Integer> getLogout() {
        return logout;
    }

    public LiveData<String> getThrowable() {
        return throwable;
    }

    public LiveData<DataProfile> getProfile() {
        return profile;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public void profileRequest() {
        Timber.d("Send profile request");
        Disposable disposable = repository.profileRequest(userToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError);
        disposables.add(disposable);
    }

    public void logoutRequest() {
        Timber.d("Send logout request");
        Disposable disposable = repository.logoutRequest(userToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError);
        disposables.add(disposable);
    }

    private void onSuccess(ResponseProfile response) {
        int status = response.getStatus();
        Timber.d("Profile request status %d", status);

        switch (status) {
            case 200:
                profile.setValue(response.getData());
                break;
            case 500:
                errorMessage.setValue(response.getErrorMsg());
                break;
        }
    }

    private void onSuccess(ResponseBase response) {
        int status = response.getStatus();
        Timber.d("Logout request status %d", status);

        switch (status) {
            case 200:
                logout.setValue(status);
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
