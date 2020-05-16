package com.supercasual.fourtop.uimain;

import androidx.lifecycle.MutableLiveData;

import com.supercasual.fourtop.network.ApiFactory;
import com.supercasual.fourtop.network.pojo.AppResponse;
import com.supercasual.fourtop.network.pojo.ResponseImages;
import com.supercasual.fourtop.network.pojo.ResponseBase;
import com.supercasual.fourtop.network.pojo.ResponseVoting;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {

    public MutableLiveData<AppResponse> voteCreateRequest(String token) {
        MutableLiveData<AppResponse> liveData = new MutableLiveData<>();
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);

        Call<ResponseVoting> call = ApiFactory.getApiFactory().getApiService().voteCreate(tokenBody);
        call.enqueue(new Callback<ResponseVoting>() {
                         @Override
                         public void onResponse(Call<ResponseVoting> call, Response<ResponseVoting> response) {
                             liveData.setValue(new AppResponse(response.body().getData()));
                         }

                         @Override
                         public void onFailure(Call<ResponseVoting> call, Throwable t) {
                             liveData.setValue(new AppResponse("Error"));
                             t.printStackTrace();
                         }
                     }
        );
        return liveData;
    }

    public MutableLiveData<AppResponse> voteRequest(String userToken, String voteToken, String vote) {
        MutableLiveData<AppResponse> liveData = new MutableLiveData<>();
        RequestBody userTokenBody = RequestBody.create(MediaType.parse("text/plain"), userToken);
        RequestBody voteTokenBody = RequestBody.create(MediaType.parse("text/plain"), voteToken);
        RequestBody voteBody = RequestBody.create(MediaType.parse("text/plain"), vote);

        Call<ResponseBase> call = ApiFactory.getApiFactory().getApiService()
                .vote(userTokenBody, voteTokenBody, voteBody);
        call.enqueue(new Callback<ResponseBase>() {
                         @Override
                         public void onResponse(Call<ResponseBase> call, Response<ResponseBase> response) {
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
                         public void onFailure(Call<ResponseBase> call, Throwable t) {
                             t.printStackTrace();
                         }
                     }
        );
        return liveData;
    }
}
