package com.supercasual.fourtop.uiauth;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.supercasual.fourtop.network.pojo.AppResponse;
import com.supercasual.fourtop.utils.Constants;

public class LoginViewModel extends ViewModel {

    private MediatorLiveData<AppResponse> liveData;
    private AuthRepository repository;

    public LoginViewModel() {
        liveData = new MediatorLiveData<>();
        repository = new AuthRepository();
    }

    public void clearLiveDara() {
        liveData = new MediatorLiveData<>();
    }

    public void saveUserToken(String userToken, SharedPreferences sp) {
        Editor editor = sp.edit();
        editor.putString(Constants.SHARED_TOKEN, userToken);
        editor.apply();
    }

    public LiveData<AppResponse> login(String login, String password) {
        liveData.addSource(
                repository.loginRequest(login, password),
                appResponse -> liveData.setValue(appResponse)
        );
        return liveData;
    }
}
