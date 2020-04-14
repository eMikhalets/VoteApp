package com.supercasual.fourtop.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.supercasual.fourtop.utils.Constants;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<String> userToken;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getUserToken() {
        if (userToken == null) {
            userToken = new MutableLiveData<>();
            loadData();
        }
        return userToken;
    }

    public void setUserToken(MutableLiveData<String> userToken) {
        this.userToken = userToken;
    }

    private void loadData() {
        SharedPreferences sp = getApplication()
                .getSharedPreferences(Constants.SHARED_FILE, Context.MODE_PRIVATE);
        userToken.setValue(sp.getString(Constants.SHARED_TOKEN, ""));
    }
}
