package com.supercasual.fourtop.uiauth.login;

import com.supercasual.fourtop.network.ApiFactory;
import com.supercasual.fourtop.network.pojo.ResponseToken;

import org.jetbrains.annotations.NotNull;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    public interface RequestCallback {
        void result(String result);
    }

    public void loginRequest(String login, String password, RequestCallback callback) {
        RequestBody loginBody = RequestBody.create(MediaType.parse("text/plain"), login);
        RequestBody passBody = RequestBody.create(MediaType.parse("text/plain"), password);

        ApiFactory.getApiFactory().getApiService()
                .login(loginBody, passBody)
                .enqueue(new Callback<ResponseToken>() {
                     @Override
                     public void onResponse(@NotNull Call<ResponseToken> call,
                                            @NotNull Response<ResponseToken> response) {
                         ResponseToken responseToken = response.body();

                         if (responseToken != null) {
                             int code = responseToken.getStatus();

                             if (code == 200) {
                                 callback.result(
                                         responseToken.getData().getUserToken());
                             } else {
                                 callback.result("Code: " + code);
                             }
                         }
                     }

                     @Override
                     public void onFailure(@NotNull Call<ResponseToken> call, @NotNull Throwable t) {
                         t.printStackTrace();
                         callback.result("Throwable: " + t);
                     }
                 }
        );
    }
}
