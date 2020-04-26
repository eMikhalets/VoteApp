package com.supercasual.fourtop.uimain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MediatorLiveData<List<String>> liveData;
    private MainRepository repository;

    public HomeViewModel() {
        liveData = new MediatorLiveData<>();
        repository = new MainRepository();
    }

    public LiveData<List<String>> getData() {
        liveData.addSource(
                repository.getHomeData(),
                strings -> liveData.setValue(strings)
        );
        return liveData;
    }
}
