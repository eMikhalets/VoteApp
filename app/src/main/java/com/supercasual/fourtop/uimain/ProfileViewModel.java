package com.supercasual.fourtop.uimain;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.supercasual.fourtop.network.pojo.AppResponse;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<AppResponse> liveData;
    private MainRepository repository;

    public ProfileViewModel() {
        liveData = new MediatorLiveData<>();
        repository = new MainRepository();
    }

    public MutableLiveData<AppResponse> getLiveData() {
        return liveData;
    }

    public void profile(String token) {
        liveData = repository.profileRequest(token);
    }

    public void logout(String token) {
        liveData = repository.logoutRequest(token);
    }
}
