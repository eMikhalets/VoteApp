package com.supercasual.fourtop.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.supercasual.fourtop.network.ApiFactory;
import com.supercasual.fourtop.network.pojo.ApiResponse;
import com.supercasual.fourtop.network.pojo.ImagesData;
import com.supercasual.fourtop.network.pojo.VoteData;
import com.supercasual.fourtop.network.pojo.VoteResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VotingViewModel extends ViewModel {

    private MutableLiveData<List<ImagesData>> liveDataImages;
    private MutableLiveData<String> liveDataVoteToken;

    public VotingViewModel() {
        liveDataImages = new MutableLiveData<>();
        liveDataImages.setValue(new ArrayList<>());
        liveDataVoteToken = new MutableLiveData<>();
        liveDataVoteToken.setValue("");
    }

    public MutableLiveData<List<ImagesData>> getLiveDataImages() {
        return liveDataImages;
    }

    public MutableLiveData<String> getLiveDataVoteToken() {
        return liveDataVoteToken;
    }

    public void clearLiveData() {
        liveDataImages.setValue(new ArrayList<>());
        liveDataVoteToken.setValue("");
    }

    public void sendVoteCreateRequest(String token) {

        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);

        Call<VoteResponse> responseCall = ApiFactory.getApiFactory().getApiService().voteCreate(tokenBody);
        responseCall.enqueue(new Callback<VoteResponse>() {
                                 @Override
                                 public void onResponse(Call<VoteResponse> call, Response<VoteResponse> response) {
                                     // HTTP code is always == 200
                                     // check JSON "status"
                                     int code = response.body().getStatus();

                                     if (code == 200) {
                                         VoteData voteData = response.body().getData();
                                         liveDataImages.setValue(voteData.getImages());
                                         liveDataVoteToken.setValue(voteData.getVoteToken());
                                     }
                                 }

                                 @Override
                                 public void onFailure(Call<VoteResponse> call, Throwable t) {
                                     t.printStackTrace();
                                 }
                             }
        );
    }

    public void sendVoteRequest(String token, String voteToken, int vote) {

        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody voteTokenBody = RequestBody.create(MediaType.parse("text/plain"), voteToken);
        RequestBody voteBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(vote));

        Call<ApiResponse> responseCall = ApiFactory.getApiFactory().getApiService().vote(tokenBody, voteTokenBody, voteBody);
        responseCall.enqueue(new Callback<ApiResponse>() {
                                 @Override
                                 public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                 }

                                 @Override
                                 public void onFailure(Call<ApiResponse> call, Throwable t) {
                                     t.printStackTrace();
                                 }
                             }
        );
    }
}
