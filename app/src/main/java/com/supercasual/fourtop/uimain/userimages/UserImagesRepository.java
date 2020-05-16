package com.supercasual.fourtop.uimain.userimages;

import com.supercasual.fourtop.network.ApiFactory;
import com.supercasual.fourtop.network.pojo.DataImage;
import com.supercasual.fourtop.network.pojo.ResponseBase;
import com.supercasual.fourtop.network.pojo.ResponseImages;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserImagesRepository {

    public interface GalleryRequestCallback {
        void success(List<DataImage> images);
        void failure(String result);
    }

    public interface GalleryChangeRequestCallback {
        void result(String result);
    }

    public void galleryRequest(String token, String count, String offset,
                               GalleryRequestCallback callback) {
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody countBody = RequestBody.create(MediaType.parse("text/plain"), count);
        RequestBody offsetBody = RequestBody.create(MediaType.parse("text/plain"), offset);

        ApiFactory.getApiFactory().getApiService()
                .gallery(tokenBody, countBody, offsetBody)
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

    public void galleryAddRequest(String token, File file,
                                  GalleryChangeRequestCallback callback) {
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);

        ApiFactory.getApiFactory().getApiService()
                .galleryAdd(tokenBody, fileBody)
                .enqueue(new Callback<ResponseBase>() {
                     @Override
                     public void onResponse(@NotNull Call<ResponseBase> call,
                                            @NotNull Response<ResponseBase> response) {
                         ResponseBase responseBase = response.body();

                         if (responseBase != null) {
                             int code = responseBase.getStatus();

                             if (code == 200) {
                                 callback.result("OK");
                             } else {
                                 callback.result("Code: " + code);
                             }
                         }
                     }

                     @Override
                     public void onFailure(Call<ResponseBase> call, Throwable t) {
                         t.printStackTrace();
                         callback.result("Throwable" + t);
                     }
                 }
        );
    }

    public void galleryRemoveRequest(String token, String id,
                                     GalleryChangeRequestCallback callback) {
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody idBody = RequestBody.create(MediaType.parse("text/plain"), id);

        ApiFactory.getApiFactory().getApiService()
                .galleryRemove(tokenBody, idBody)
                .enqueue(new Callback<ResponseBase>() {
                     @Override
                     public void onResponse(@NotNull Call<ResponseBase> call,
                                            @NotNull Response<ResponseBase> response) {
                         ResponseBase responseBase = response.body();

                         if (responseBase != null) {
                             int code = responseBase.getStatus();

                             if (code == 200) {
                                 callback.result("OK");
                             } else {
                                 callback.result("Code: " + code);
                             }
                         }
                     }

                     @Override
                     public void onFailure(Call<ResponseBase> call, Throwable t) {
                         t.printStackTrace();
                         callback.result("Throwable" + t);
                     }
                 }
        );
    }
}
