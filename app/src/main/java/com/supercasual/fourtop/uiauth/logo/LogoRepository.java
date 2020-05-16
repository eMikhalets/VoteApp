package com.supercasual.fourtop.uiauth.logo;

import com.supercasual.fourtop.network.ApiFactory;
import com.supercasual.fourtop.network.pojo.ResponseBase;

import org.jetbrains.annotations.NotNull;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogoRepository {

    public interface RequestCallback {
        void result(String result);
    }

    public void tokenRequest(String token, RequestCallback callback) {
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);

        ApiFactory.getApiFactory().getApiService()
                .token(tokenBody)
                .enqueue(new Callback<ResponseBase>() {
                     @Override
                     public void onResponse(@NotNull Call<ResponseBase> call,
                                            @NotNull Response<ResponseBase> response) {
                         ResponseBase responseBase = response.body();

                         if (responseBase != null) {
                             int code = responseBase.getStatus();

                             if (responseBase.getStatus() == 200) {
                                 callback.result("VALID");
                             } else if (code == 403) {
                                 callback.result("INVALID");
                             } else {
                                 callback.result("Code: " + code);
                             }
                         }
                     }

                     @Override
                     public void onFailure(@NotNull Call<ResponseBase> call, @NotNull Throwable t) {
                         t.printStackTrace();
                         callback.result("Throwable" + t);
                     }
                 }
        );
    }
}
