package com.supercasual.fourtop.uimain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.supercasual.fourtop.network.pojo.AppResponse;

public class VotingViewModel extends ViewModel {

    private MediatorLiveData<AppResponse> liveData;
    private MainRepository repository;

    public VotingViewModel() {
        liveData = new MediatorLiveData<>();
        repository = new MainRepository();
    }

    public MediatorLiveData<AppResponse> getLiveData() {
        return liveData;
    }

    public void clearLiveData() {
        liveData = new MediatorLiveData<>();
    }

    public LiveData<AppResponse> voteCreate(String token) {
        liveData.addSource(
                repository.voteCreateRequest(token),
                appResponse -> liveData.setValue(appResponse)
        );
        return liveData;
    }

    public LiveData<AppResponse> vote(String token, String voteToken, String vote) {
        liveData.addSource(
                repository.voteRequest(token, voteToken, vote),
                appResponse -> liveData.setValue(appResponse)
        );
        return liveData;
    }
}
