package com.supercasual.fourtop.uimain.profile;

import com.supercasual.fourtop.network.ApiFactory;
import com.supercasual.fourtop.network.pojo.DataProfile;
import com.supercasual.fourtop.network.pojo.ResponseBase;
import com.supercasual.fourtop.network.pojo.ResponseProfile;

import org.jetbrains.annotations.NotNull;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {

    public interface ProfileRequestCallback {
        void success(DataProfile dataProfile);

        void failure(String result);
    }

    public interface LogoutRequestCallback {
        void result(String result);
    }

    public void profileRequest(String token, ProfileRequestCallback callback) {
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);

        ApiFactory.getApiFactory().getApiService()
                .profile(tokenBody)
                .enqueue(new Callback<ResponseProfile>() {
                     @Override
                     public void onResponse(@NotNull Call<ResponseProfile> call,
                                            @NotNull Response<ResponseProfile> response) {
                         ResponseProfile responseProfile = response.body();

                         if (responseProfile != null) {

                             int code = responseProfile.getStatus();

                             if (code == 200) {
                                 callback.success(responseProfile.getData());
                             } else {
                                 callback.failure("Code: " + code);
                             }
                         }
                     }

                     @Override
                     public void onFailure(Call<ResponseProfile> call, Throwable t) {
                         t.printStackTrace();
                         callback.failure("Throwable: " + t);
                     }
                 }
        );
    }

    public void logoutRequest(String token, LogoutRequestCallback callback) {
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);

        ApiFactory.getApiFactory().getApiService()
                .logout(tokenBody)
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
                         callback.result("Throwable: " + t);
                     }
                 }
        );
    }
}
