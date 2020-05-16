package com.supercasual.fourtop.uimain.voting;

import com.supercasual.fourtop.network.ApiFactory;
import com.supercasual.fourtop.network.pojo.DataVoting;
import com.supercasual.fourtop.network.pojo.ResponseBase;
import com.supercasual.fourtop.network.pojo.ResponseVoting;

import org.jetbrains.annotations.NotNull;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VotingRepository {

    public interface VoteCreateCallback {
        void success(DataVoting voting);
        void failure(String result);
    }

    public interface VotingRequestCallback {
        void result(String result);
    }

    public void voteCreateRequest(String token, VoteCreateCallback callback) {
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);

        ApiFactory.getApiFactory().getApiService()
                .voteCreate(tokenBody)
                .enqueue(new Callback<ResponseVoting>() {
                     @Override
                     public void onResponse(@NotNull Call<ResponseVoting> call,
                                            @NotNull Response<ResponseVoting> response) {
                         ResponseVoting responseVoting = response.body();

                         if (responseVoting != null) {
                             int code = responseVoting.getStatus();

                             if (code == 200) {
                                 callback.success(responseVoting.getData());
                             } else {
                                 callback.failure("Code: " + code);
                             }
                         }
                     }

                     @Override
                     public void onFailure(@NotNull Call<ResponseVoting> call, @NotNull Throwable t) {
                         t.printStackTrace();
                         callback.failure("Throwable" + t);
                     }
                 }
        );
    }

    public void voteRequest(String userToken, String voteToken, String vote,
                            VotingRequestCallback callback) {
        RequestBody userTokenBody = RequestBody.create(MediaType.parse("text/plain"), userToken);
        RequestBody voteTokenBody = RequestBody.create(MediaType.parse("text/plain"), voteToken);
        RequestBody voteBody = RequestBody.create(MediaType.parse("text/plain"), vote);

        ApiFactory.getApiFactory().getApiService()
                .vote(userTokenBody, voteTokenBody, voteBody)
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
