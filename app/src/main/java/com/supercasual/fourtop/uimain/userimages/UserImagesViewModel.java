package com.supercasual.fourtop.uimain.userimages;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.supercasual.fourtop.network.pojo.DataImage;

import java.io.File;
import java.util.List;

public class UserImagesViewModel extends ViewModel {

    private UserImagesRepository userImagesRepository;

    private MutableLiveData<List<DataImage>> apiGallery;
    private MutableLiveData<String> apiGalleryAdd;
    private MutableLiveData<String> apiGalleryRemove;

    private MutableLiveData<String> token;
    private MutableLiveData<File> file;

    public UserImagesViewModel() {
        userImagesRepository = new UserImagesRepository();

        apiGallery = new MutableLiveData<>();
        apiGalleryAdd = new MutableLiveData<>();
        apiGalleryRemove = new MutableLiveData<>();

        token = new MutableLiveData<>();
        file = new MutableLiveData<>();
    }

    public LiveData<List<DataImage>> getApiGallery() {
        return apiGallery;
    }

    public LiveData<String> getApiGalleryAdd() {
        return apiGalleryAdd;
    }

    public LiveData<String> getApiGalleryRemove() {
        return apiGalleryRemove;
    }

    public MutableLiveData<String> getToken() {
        return token;
    }

    public MutableLiveData<File> getFile() {
        return file;
    }

    public void galleryRequest() {
        String token = getToken().getValue();
        String count = String.valueOf(10);
        String offset = String.valueOf(0);

        userImagesRepository.galleryRequest(token, count, offset,
                new UserImagesRepository.GalleryRequestCallback() {
                    @Override
                    public void success(List<DataImage> images) {
                        apiGallery.setValue(images);
                    }

                    @Override
                    public void failure(String result) {
                    }
                });
    }

    public void galleryAddRequest(String token, File file) {
        userImagesRepository.galleryAddRequest(token, file,
                result -> apiGalleryAdd.setValue(result));
    }

    public void galleryRemoveRequest(String token, String id) {
        userImagesRepository.galleryRemoveRequest(token, id, result ->
                apiGalleryRemove.setValue(result));
    }
}
