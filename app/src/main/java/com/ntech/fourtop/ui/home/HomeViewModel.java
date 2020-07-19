package com.ntech.fourtop.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntech.fourtop.data.HomeRepository;

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
