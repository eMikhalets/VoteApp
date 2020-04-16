package com.supercasual.fourtop.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.supercasual.fourtop.adapter.ImageAdapter;
import com.supercasual.fourtop.network.ApiFactory;
import com.supercasual.fourtop.network.pojo.ImagesData;
import com.supercasual.fourtop.network.pojo.ImagesResponse;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserImagesViewModel extends ViewModel {

    private MutableLiveData<List<ImagesData>> liveData;

    public UserImagesViewModel() {
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<ImagesData>> getLiveData() {
        return liveData;
    }

    public void setLiveData(MutableLiveData<List<ImagesData>> liveData) {
        this.liveData = liveData;
    }

    public void sendGalleryRequest(String token, ImageAdapter adapter) {
        String count = String.valueOf(10);
        String offset = String.valueOf(0);

        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody countBody = RequestBody.create(MediaType.parse("text/plain"), count);
        RequestBody offsetBody = RequestBody.create(MediaType.parse("text/plain"), offset);

        Call<ImagesResponse> responseCall = ApiFactory.getApiFactory().getApiService().gallery(tokenBody, countBody, offsetBody);
        responseCall.enqueue(new Callback<ImagesResponse>() {
                                 @Override
                                 public void onResponse(Call<ImagesResponse> call, Response<ImagesResponse> response) {
                                     // HTTP code is always == 200
                                     // check JSON "status"
                                     int code = response.body().getStatus();

                                     if (code == 200) {
                                         List<ImagesData> images = response.body().getData();
                                         adapter.setImages(images);
                                     }
                                 }

                                 @Override
                                 public void onFailure(Call<ImagesResponse> call, Throwable t) {
                                     t.printStackTrace();
                                 }
                             }
        );
    }
}
