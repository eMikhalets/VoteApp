package com.ntech.fourtop.ui.topusers;

import androidx.lifecycle.ViewModel;

import com.ntech.fourtop.data.AppRepository;

public class TopUsersViewModel extends ViewModel {

    private AppRepository repository;

    public TopUsersViewModel() {
        repository = AppRepository.get();
    }
}
