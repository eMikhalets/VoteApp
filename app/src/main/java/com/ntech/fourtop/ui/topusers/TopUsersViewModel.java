package com.ntech.fourtop.ui.topusers;

import androidx.lifecycle.ViewModel;

import com.ntech.fourtop.data.AppRepository;

public class TopUsersViewModel extends ViewModel {

    private AppRepository repository;
    private String userToken = "";

    public TopUsersViewModel() {
        repository = AppRepository.get();
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
