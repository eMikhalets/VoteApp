package com.supercasual.fourtop.uimain;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.supercasual.fourtop.network.pojo.AppResponse;
import com.supercasual.fourtop.utils.Constants;

public class ProfileViewModel extends ViewModel {

    private MediatorLiveData<AppResponse> liveData;
    private MainRepository repository;

    public ProfileViewModel() {
        liveData = new MediatorLiveData<>();
        repository = new MainRepository();
    }

    public void deleteUserToken(SharedPreferences sp) {
        Editor editor = sp.edit();
        editor.putString(Constants.SHARED_TOKEN, "");
        editor.apply();
    }

    public LiveData<AppResponse> logout(String token) {
        liveData.addSource(
                repository.logoutRequest(token),
                appResponse -> liveData.setValue(appResponse)
        );
        return liveData;
    }
}
