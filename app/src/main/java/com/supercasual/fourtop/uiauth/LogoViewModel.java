package com.supercasual.fourtop.uiauth;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.supercasual.fourtop.network.pojo.AppResponse;
import com.supercasual.fourtop.utils.Constants;

public class LogoViewModel extends ViewModel {

    private MediatorLiveData<AppResponse> liveData;
    private AuthRepository repository;

    public LogoViewModel() {
        liveData = new MediatorLiveData<>();
        repository = new AuthRepository();
    }

    public String loadUserToken(SharedPreferences sp) {
        return sp.getString(Constants.SHARED_TOKEN, "");
    }

    public LiveData<AppResponse> checkUserToken(String userToken) {
        liveData.addSource(
                repository.tokenRequest(userToken),
                appResponse -> liveData.setValue(appResponse)
        );
        return liveData;
    }
}
