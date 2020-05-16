package com.supercasual.fourtop.uimain.topimages;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.supercasual.fourtop.network.pojo.DataImage;

import java.util.List;

public class TopImagesViewModel extends ViewModel {

    private TopImagesRepository topImagesRepository;

    private MutableLiveData<List<DataImage>> apiTopImages;

    private MutableLiveData<String> token;

    public TopImagesViewModel() {
        topImagesRepository = new TopImagesRepository();

        apiTopImages = new MutableLiveData<>();

        token = new MutableLiveData<>();
    }

    public LiveData<List<DataImage>> getApiTopImages() {
        return apiTopImages;
    }

    public MutableLiveData<String> getToken() {
        return token;
    }

    public void topPhotosRequest() {
        String token = getToken().getValue();
        String count = String.valueOf(10);
        String offset = String.valueOf(0);

        topImagesRepository.topPhotosRequest(token, count, offset,
                new TopImagesRepository.RequestCallback() {
                    @Override
                    public void success(List<DataImage> images) {
                        apiTopImages.setValue(images);
                    }

                    @Override
                    public void failure(String result) {
                    }
                });
    }
}
