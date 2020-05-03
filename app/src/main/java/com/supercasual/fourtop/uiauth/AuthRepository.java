package com.supercasual.fourtop.uiauth;

import androidx.lifecycle.LiveData;
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

/**
 * Send Requests:
 * /api/token
 * /api/login
 * /api/register
 * /api/register/email
 * /api/register//login
 * <p>
 * Load and save user token in SharedPreferences
 */
public class AuthRepository {

    public LiveData<AppResponse> tokenRequest(String userToken) {
        MutableLiveData<AppResponse> liveData = new MutableLiveData<>();
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), userToken);

        Call<ResponseSimple> call = ApiFactory.getApiFactory().getApiService().token(requestBody);
        call.enqueue(new Callback<ResponseSimple>() {
                         @Override
                         public void onResponse(Call<ResponseSimple> call, Response<ResponseSimple> response) {
                             int code = response.body().getStatus();

                             switch (code) {
                                 case 200:
                                     liveData.setValue(new AppResponse(userToken));
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

    public LiveData<AppResponse> loginRequest(String login, String password) {
        MutableLiveData<AppResponse> liveData = new MutableLiveData<>();
        RequestBody loginBody = RequestBody.create(MediaType.parse("text/plain"), login);
        RequestBody passBody = RequestBody.create(MediaType.parse("text/plain"), password);

        Call<ResponseToken> call = ApiFactory.getApiFactory().getApiService()
                .login(loginBody, passBody);
        call.enqueue(new Callback<ResponseToken>() {
                         @Override
                         public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                             liveData.setValue(new AppResponse(response.body().getData()));
                         }

                         @Override
                         public void onFailure(Call<ResponseToken> call, Throwable t) {
                             liveData.setValue(new AppResponse("Ошибка авторизации"));
                             t.printStackTrace();
                         }
                     }
        );
        return liveData;
    }

    public LiveData<AppResponse> registerRequest(String email, String login, String password, String name) {
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

    public LiveData<AppResponse> registerEmailRequest(String email) {
        MutableLiveData<AppResponse> liveData = new MutableLiveData<>();
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);

        Call<ResponseSimple> call = ApiFactory.getApiFactory().getApiService().registerEmail(emailBody);
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

    public LiveData<AppResponse> registerLoginRequest(String login) {
        MutableLiveData<AppResponse> liveData = new MutableLiveData<>();
        RequestBody loginBody = RequestBody.create(MediaType.parse("text/plain"), login);

        Call<ResponseSimple> call = ApiFactory.getApiFactory().getApiService().registerLogin(loginBody);
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
}
