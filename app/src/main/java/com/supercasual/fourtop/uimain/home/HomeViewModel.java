package com.supercasual.fourtop.uimain.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private HomeRepository homeRepository;
    private MutableLiveData<String> api;

    public HomeViewModel() {
        homeRepository = new HomeRepository();
        api = new MutableLiveData<>();
    }

    public LiveData<String> getApi() {
        return api;
    }
}
