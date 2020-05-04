package com.supercasual.fourtop.uiauth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.supercasual.fourtop.network.pojo.AppResponse;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<AppResponse> liveData;
    private AuthRepository repository;

    public LoginViewModel() {
        liveData = new MutableLiveData<>();
        repository = new AuthRepository();
    }

    public LiveData<AppResponse> getLiveData() {
        return liveData;
    }

    public void login(String login, String password) {
        liveData = repository.loginRequest(login, password);
    }
}
