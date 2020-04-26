package com.supercasual.fourtop.uimain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.supercasual.fourtop.network.pojo.AppResponse;

public class TopImagesViewModel extends ViewModel {

    private MediatorLiveData<AppResponse> liveData;
    private MainRepository repository;

    public TopImagesViewModel() {
        liveData = new MediatorLiveData<>();
        repository = new MainRepository();
    }

    public MediatorLiveData<AppResponse> getLiveData() {
        return liveData;
    }

    public void setLiveData(MediatorLiveData<AppResponse> liveData) {
        this.liveData = liveData;
    }

    public LiveData<AppResponse> topPhotos(String token, String count, String offset) {
        liveData.addSource(
                repository.topPhotosRequest(token, count, offset),
                appResponse -> liveData.setValue(appResponse)
        );
        return liveData;
    }
}
