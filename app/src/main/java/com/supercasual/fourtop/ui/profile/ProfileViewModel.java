package com.supercasual.fourtop.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.supercasual.fourtop.network.CurrentUser;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(CurrentUser.getLogin());
    }

    public LiveData<String> getText() {
        return mText;
    }
}