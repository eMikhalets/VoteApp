package com.supercasual.fourtop.uimain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.supercasual.fourtop.network.pojo.AppResponse;

import java.io.File;

public class UserImagesViewModel extends ViewModel {

    private MediatorLiveData<AppResponse> liveData;
    private MainRepository repository;

    public UserImagesViewModel() {
        liveData = new MediatorLiveData<>();
        repository = new MainRepository();
    }

    public MediatorLiveData<AppResponse> getLiveData() {
        return liveData;
    }

    public LiveData<AppResponse> gallery(String token, String count, String offset) {
        liveData.addSource(
                repository.galleryRequest(token, count, offset),
                appResponse -> liveData.setValue(appResponse)
        );
        return liveData;
    }

//    public LiveData<AppResponse> galleryAdd(String token, File file) {
//        liveData.addSource(
//                repository.galleryAddRequest(token, file),
//                appResponse -> liveData.setValue(appResponse)
//        );
//        return liveData;
//    }

    public LiveData<AppResponse> galleryRemove(String token, String id) {
        liveData.addSource(
                repository.galleryRemoveRequest(token, id),
                appResponse -> liveData.setValue(appResponse)
        );
        return liveData;
    }
}
