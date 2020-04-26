package com.supercasual.fourtop.uiauth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.supercasual.fourtop.network.pojo.AppResponse;

public class RegisterViewModel extends ViewModel {

    private MediatorLiveData<AppResponse> liveData;
    private AuthRepository repository;

    public RegisterViewModel() {
        liveData = new MediatorLiveData<>();
        repository = new AuthRepository();
    }

    public LiveData<AppResponse> register(String email, String login, String password, String name) {
        liveData.addSource(
                repository.registerRequest(email, login, password, name),
                appResponse -> liveData.setValue(appResponse)
        );
        return liveData;
    }
}
