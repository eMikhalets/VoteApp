package com.ntech.fourtop.ui.voting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntech.fourtop.data.AppRepository;
import com.ntech.fourtop.network.pojo.ResponseBase;
import com.ntech.fourtop.network.pojo.ResponseVoting;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class VotingViewModel extends ViewModel {

    private AppRepository repository;
    private CompositeDisposable disposables;
    private MutableLiveData<String> throwable;
    private MutableLiveData<Integer> voting;
    private MutableLiveData<String> errorMessage;
    private String userToken;
    private String voteToken;

    public VotingViewModel() {
        repository = AppRepository.get();
        disposables = new CompositeDisposable();
        throwable = new MutableLiveData<>();
        voting = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        userToken = "";
        voteToken = "";
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public LiveData<String> getThrowable() {
        return throwable;
    }

    public LiveData<Integer> getVoting() {
        return voting;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public void voteCreateRequest() {
        Timber.d("Send vote create request");
        Disposable disposable = repository.voteCreateRequest(userToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError);
        disposables.add(disposable);
    }

    public void voteRequest(int voteValue) {
        String vote = String.valueOf(voteValue);
        Timber.d("Send vote request");
        Disposable disposable = repository.voteRequest(userToken, voteToken, vote)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError);
        disposables.add(disposable);
    }

    private void onSuccess(ResponseVoting response) {
        int status = response.getStatus();
        Timber.d("Vote create request status %d", status);
        switch (status) {
            case 200:
                voteToken = response.getData().getVoteToken();
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
                voting.setValue(status);
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
