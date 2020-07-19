package com.ntech.fourtop.ui.logo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogoViewModel extends ViewModel {

    private LogoRepository logoRepository;
    private MutableLiveData<String> apiToken;
    private MutableLiveData<String> userToken;

    public LogoViewModel() {
        logoRepository = new LogoRepository();
        apiToken = new MutableLiveData<>();
        userToken = new MutableLiveData<>();
    }

    public LiveData<String> getApiToken() {
        return apiToken;
    }

    public MutableLiveData<String> getUserToken() {
        return userToken;
    }

    public void tokenRequest(String token) {
        logoRepository.tokenRequest(token, result -> apiToken.setValue(result));
    }
}
