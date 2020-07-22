package com.ntech.fourtop.ui.home;

import androidx.lifecycle.ViewModel;

import com.ntech.fourtop.data.AppRepository;

public class HomeViewModel extends ViewModel {

    private AppRepository repository;

    public HomeViewModel() {
        repository = AppRepository.get();
    }
}
