package com.ntech.fourtop.ui.logo;

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

public class LogoViewModel extends ViewModel {

    private AppRepository repository;
    private CompositeDisposable disposables;
    private MutableLiveData<String> throwable;
    private MutableLiveData<String> errorMessage;
    private MutableLiveData<Integer> liveDataResponse;

    public LogoViewModel() {
        repository = AppRepository.get();
        disposables = new CompositeDisposable();
        throwable = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        liveDataResponse = new MutableLiveData<>();
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

    public LiveData<Integer> getLiveDataResponse() {
        return liveDataResponse;
    }

    public void tokenRequest(String token) {
        Timber.d("Send token request");
        Disposable disposable = repository.tokenRequest(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError);
        disposables.add(disposable);
    }

    private void onSuccess(ResponseBase response) {
        int status = response.getStatus();
        Timber.d("Request status %d", status);

        switch (status) {
            case 200:
                liveDataResponse.setValue(status);
                break;
            case 403:
                errorMessage.setValue(response.getErrorMsg());
                break;
        }
    }

    private void onError(Throwable t) {
        Timber.d(t);
        throwable.setValue(t.toString());
    }
}
