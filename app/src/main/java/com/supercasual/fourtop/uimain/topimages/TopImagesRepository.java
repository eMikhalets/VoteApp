package com.supercasual.fourtop.uimain.topimages;

import androidx.lifecycle.MutableLiveData;

import com.supercasual.fourtop.network.ApiFactory;
import com.supercasual.fourtop.network.pojo.AppResponse;
import com.supercasual.fourtop.network.pojo.DataImage;
import com.supercasual.fourtop.network.pojo.ResponseImages;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopImagesRepository {

    public interface RequestCallback {
        void success(List<DataImage> images);
        void failure(String result);
    }

    public void topPhotosRequest(String token, String count, String offset,
                                 RequestCallback callback) {
        MutableLiveData<AppResponse> liveData = new MutableLiveData<>();
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody countBody = RequestBody.create(MediaType.parse("text/plain"), count);
        RequestBody offsetBody = RequestBody.create(MediaType.parse("text/plain"), offset);

        ApiFactory.getApiFactory().getApiService()
                .topPhotos(tokenBody, countBody, offsetBody)
                .enqueue(new Callback<ResponseImages>() {
                         @Override
                         public void onResponse(@NotNull Call<ResponseImages> call,
                                                @NotNull Response<ResponseImages> response) {
                             ResponseImages responseImages = response.body();

                             if (responseImages != null) {
                                 int code = responseImages.getStatus();

                                 if (code == 200) {
                                     callback.success(responseImages.getData());
                                 } else {
                                     callback.failure("Code: " + code);
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<ResponseImages> call, Throwable t) {
                             t.printStackTrace();
                             callback.failure("Throwable" + t);
                         }
                     }
        );
    }
}
