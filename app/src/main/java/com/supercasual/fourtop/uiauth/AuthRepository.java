package com.supercasual.fourtop.uiauth;

import androidx.lifecycle.MutableLiveData;

import com.supercasual.fourtop.network.ApiFactory;
import com.supercasual.fourtop.network.pojo.AppResponse;
import com.supercasual.fourtop.network.pojo.ResponseSimple;
import com.supercasual.fourtop.network.pojo.ResponseToken;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    public MutableLiveData<AppResponse> tokenRequest(String token) {
        MutableLiveData<AppResponse> liveData = new MutableLiveData<>();
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        Call<ResponseSimple> call = ApiFactory.getApiFactory().getApiService()
                .token(tokenBody);
        call.enqueue(new Callback<ResponseSimple>() {
                         @Override
                         public void onResponse(Call<ResponseSimple> call, Response<ResponseSimple> response) {
                             int code = response.body().getStatus();

                             if (code == 200) {
                                 liveData.setValue(new AppResponse(token));
                             } else {
                                 liveData.setValue(new AppResponse(""));
                             }
                         }

                         @Override
                         public void onFailure(Call<ResponseSimple> call, Throwable t) {
                             t.printStackTrace();
                         }
                     }
        );
        return liveData;
    }

    public MutableLiveData<AppResponse> loginRequest(String login, String password) {
        MutableLiveData<AppResponse> liveData = new MutableLiveData<>();
        RequestBody loginBody = RequestBody.create(MediaType.parse("text/plain"), login);
        RequestBody passBody = RequestBody.create(MediaType.parse("text/plain"), password);
        Call<ResponseToken> call = ApiFactory.getApiFactory().getApiService()
                .login(loginBody, passBody);
        call.enqueue(new Callback<ResponseToken>() {
                         @Override
                         public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                             int code = response.body().getStatus();

                             if (code == 200) {
                                 liveData.setValue(new AppResponse(response.body().getData()));
                             } else {
                                 liveData.setValue(new AppResponse(""));
                             }
                         }

                         @Override
                         public void onFailure(Call<ResponseToken> call, Throwable t) {
                             t.printStackTrace();
                         }
                     }
        );
        return liveData;
    }

    public MutableLiveData<AppResponse> registerRequest(String email, String login, String password, String name) {
        MutableLiveData<AppResponse> liveData = new MutableLiveData<>();
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody loginBody = RequestBody.create(MediaType.parse("text/plain"), login);
        RequestBody passBody = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        Call<ResponseSimple> call = ApiFactory.getApiFactory().getApiService()
                .register(emailBody, loginBody, passBody, nameBody);
        call.enqueue(new Callback<ResponseSimple>() {
                         @Override
                         public void onResponse(Call<ResponseSimple> call, Response<ResponseSimple> response) {
                             int code = response.body().getStatus();

                             if (code == 200) {
                                 liveData.setValue(new AppResponse("success"));
                             } else {
                                 liveData.setValue(new AppResponse("failure"));
                             }

                             switch (code) {
                                 case 200:
                                     liveData.setValue(new AppResponse(response.body().getData()));
                                     break;
                                 default:
                                     liveData.setValue(new AppResponse(String.valueOf(code)));
                                     break;
                             }
                         }

                         @Override
                         public void onFailure(Call<ResponseSimple> call, Throwable t) {
                             t.printStackTrace();
                         }
                     }
        );
        return liveData;
    }

    public MutableLiveData<AppResponse> checkEmailRequest(String email) {
        MutableLiveData<AppResponse> liveData = new MutableLiveData<>();
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        Call<ResponseSimple> call = ApiFactory.getApiFactory().getApiService()
                .checkEmail(emailBody);
        call.enqueue(new Callback<ResponseSimple>() {
                         @Override
                         public void onResponse(Call<ResponseSimple> call, Response<ResponseSimple> response) {
                             int code = response.body().getStatus();

                             switch (code) {
                                 case 200:
                                     liveData.setValue(new AppResponse(response.body().getData()));
                                     break;
                                 default:
                                     liveData.setValue(new AppResponse(String.valueOf(code)));
                                     break;
                             }
                         }

                         @Override
                         public void onFailure(Call<ResponseSimple> call, Throwable t) {
                             t.printStackTrace();
                         }
                     }
        );
        return liveData;
    }

    public MutableLiveData<AppResponse> checkLoginRequest(String login) {
        MutableLiveData<AppResponse> liveData = new MutableLiveData<>();
        RequestBody loginBody = RequestBody.create(MediaType.parse("text/plain"), login);
        Call<ResponseSimple> call = ApiFactory.getApiFactory().getApiService()
                .checkLogin(loginBody);
        call.enqueue(new Callback<ResponseSimple>() {
                         @Override
                         public void onResponse(Call<ResponseSimple> call, Response<ResponseSimple> response) {
                             int code = response.body().getStatus();

                             if (code == 200) {
                                 liveData.setValue(new AppResponse("200"));
                             } else {
                                 liveData.setValue(new AppResponse("404"));
                             }
                         }

                         @Override
                         public void onFailure(Call<ResponseSimple> call, Throwable t) {
                             t.printStackTrace();
                         }
                     }
        );
        return liveData;
    }
}
