package com.supercasual.fourtop.uiauth.register;

import com.supercasual.fourtop.network.ApiFactory;
import com.supercasual.fourtop.network.pojo.ResponseBase;

import org.jetbrains.annotations.NotNull;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterRepository {

    public interface RequestCallback {
        void result(String result);
    }

    public void registerRequest(String email, String login, String password, String name,
                                RequestCallback callback) {
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody loginBody = RequestBody.create(MediaType.parse("text/plain"), login);
        RequestBody passBody = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);

        ApiFactory.getApiFactory().getApiService()
                .register(emailBody, loginBody, passBody, nameBody)
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
                     public void onFailure(@NotNull Call<ResponseBase> call, @NotNull Throwable t) {
                         t.printStackTrace();
                         callback.result("Throwable: " + t);
                     }
                 }
        );
    }

    public void checkEmailRequest(String email, RequestCallback callback) {
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);

        ApiFactory.getApiFactory().getApiService()
                .checkEmail(emailBody)
                .enqueue(new Callback<ResponseBase>() {
                     @Override
                     public void onResponse(@NotNull Call<ResponseBase> call,
                                            @NotNull Response<ResponseBase> response) {
                         ResponseBase responseBase = response.body();

                         if (responseBase != null) {
                             int code = responseBase.getStatus();

                             if (code == 404) {
                                 callback.result("OK");
                             } else {
                                 callback.result("Code: " + code);
                             }
                         }
                     }

                     @Override
                     public void onFailure(@NotNull Call<ResponseBase> call, @NotNull Throwable t) {
                         t.printStackTrace();
                         callback.result("Throwable: " + t);
                     }
                 }
        );
    }

    public void checkLoginRequest(String login, RequestCallback callback) {
        RequestBody loginBody = RequestBody.create(MediaType.parse("text/plain"), login);

        ApiFactory.getApiFactory().getApiService()
                .checkLogin(loginBody)
                .enqueue(new Callback<ResponseBase>() {
                     @Override
                     public void onResponse(@NotNull Call<ResponseBase> call,
                                            @NotNull Response<ResponseBase> response) {
                         ResponseBase responseBase = response.body();

                         if (responseBase != null) {
                             int code = responseBase.getStatus();

                             if (code == 404) {
                                 callback.result("OK");
                             } else {
                                 callback.result("Code: " + code);
                             }
                         }
                     }

                     @Override
                     public void onFailure(@NotNull Call<ResponseBase> call, @NotNull Throwable t) {
                         t.printStackTrace();
                         callback.result("Throwable: " + t);
                     }
                 }
        );
    }
}
