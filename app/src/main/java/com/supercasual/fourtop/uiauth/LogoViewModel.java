package com.supercasual.fourtop.uiauth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.supercasual.fourtop.network.pojo.AppResponse;

public class LogoViewModel extends ViewModel {

    private MutableLiveData<AppResponse> liveData;
    private AuthRepository repository;

    public LogoViewModel() {
        liveData = new MutableLiveData<>();
        repository = new AuthRepository();
    }

    public LiveData<AppResponse> getLiveData() {
        return liveData;
    }

    public void checkToken(String token) {
        liveData = repository.tokenRequest(token);
    }
}
