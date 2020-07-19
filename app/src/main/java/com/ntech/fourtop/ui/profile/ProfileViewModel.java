package com.ntech.fourtop.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntech.fourtop.data.ProfileRepository;
import com.ntech.fourtop.network.pojo.DataProfile;

public class ProfileViewModel extends ViewModel {

    private ProfileRepository profileRepository;
    private MutableLiveData<String> apiProfile;
    private MutableLiveData<String> apiLogout;
    private MutableLiveData<String> token;
    private MutableLiveData<String> name;
    private MutableLiveData<String> login;
    private MutableLiveData<String> email;

    public ProfileViewModel() {
        profileRepository = new ProfileRepository();
        apiProfile = new MutableLiveData<>();
        apiLogout = new MutableLiveData<>();
        token = new MutableLiveData<>();
        name = new MutableLiveData<>();
        login = new MutableLiveData<>();
        email = new MutableLiveData<>();
    }

    public LiveData<String> getApiProfile() {
        return apiProfile;
    }

    public LiveData<String> getApiLogout() {
        return apiLogout;
    }

    public MutableLiveData<String> getToken() {
        return token;
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public MutableLiveData<String> getLogin() {
        return login;
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public void profileRequest(String token) {
        profileRepository.profileRequest(token, new ProfileRepository.ProfileRequestCallback() {
            @Override
            public void success(DataProfile dataProfile) {
                apiProfile.setValue("OK");
                name.setValue(dataProfile.getTesterName());
                login.setValue(dataProfile.getLogin());
                email.setValue(dataProfile.getEmail());
            }

            @Override
            public void failure(String result) {
                apiProfile.setValue(result);
            }
        });
    }

    public void logoutRequest(String token) {
        profileRepository.logoutRequest(token, result -> apiLogout.setValue(result));
    }
}
