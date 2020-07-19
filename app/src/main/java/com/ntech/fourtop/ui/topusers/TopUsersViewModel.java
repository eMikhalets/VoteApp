package com.ntech.fourtop.ui.topusers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntech.fourtop.data.TopUsersRepository;

import java.util.List;

public class TopUsersViewModel extends ViewModel {

    private TopUsersRepository topUsersRepository;

    private MutableLiveData<List<String>> apiTopUsers;

    private MutableLiveData<String> token;

    public TopUsersViewModel() {
        topUsersRepository = new TopUsersRepository();

        apiTopUsers = new MutableLiveData<>();

        token = new MutableLiveData<>();
    }

    public LiveData<List<String>> getApiTopUsers() {
        return apiTopUsers;
    }

    public MutableLiveData<String> getToken() {
        return token;
    }

    public void topUsersRequest(String token) {
        topUsersRepository.topUsersRequest(token, new TopUsersRepository.RequestCallback() {
            @Override
            public void success(List<String> users) {
                apiTopUsers.setValue(users);
            }

            @Override
            public void failure(String result) {
            }
        });
    }
}
