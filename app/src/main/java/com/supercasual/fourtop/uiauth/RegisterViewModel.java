package com.supercasual.fourtop.uiauth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.supercasual.fourtop.network.pojo.AppResponse;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<AppResponse> liveDataLogin;
    private MutableLiveData<AppResponse> liveDataEmail;
    private MutableLiveData<AppResponse> liveDataRegister;
    private AuthRepository repository;

    public RegisterViewModel() {
        liveDataLogin = new MutableLiveData<>();
        liveDataEmail = new MutableLiveData<>();
        liveDataRegister = new MutableLiveData<>();
        repository = new AuthRepository();
    }

    public LiveData<AppResponse> getLiveDataLogin() {
        return liveDataLogin;
    }

    public LiveData<AppResponse> getLiveDataEmail() {
        return liveDataEmail;
    }

    public LiveData<AppResponse> getLiveDataRegister() {
        return liveDataRegister;
    }

    public void checkLogin(String login) {
        liveDataLogin = repository.checkLoginRequest(login);
    }

    public void checkEmail(String email) {
        liveDataEmail = repository.checkEmailRequest(email);
    }

    public void register(String email, String login, String password, String name) {
        liveDataRegister = repository.registerRequest(email, login, password, name);
    }
}
