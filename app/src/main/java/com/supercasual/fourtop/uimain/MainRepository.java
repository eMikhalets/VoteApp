package com.supercasual.fourtop.uimain;

import androidx.lifecycle.MutableLiveData;

import com.supercasual.fourtop.network.ApiFactory;
import com.supercasual.fourtop.network.pojo.AppResponse;
import com.supercasual.fourtop.network.pojo.ResponseImages;
import com.supercasual.fourtop.network.pojo.ResponseProfile;
import com.supercasual.fourtop.network.pojo.ResponseSimple;
import com.supercasual.fourtop.network.pojo.ResponseVoting;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {

    public MutableLiveData<AppResponse> profileRequest(String token) {
        MutableLiveData<AppResponse> liveData = new MutableLiveData<>();
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);

        Call<ResponseProfile> call = ApiFactory.getApiFactory().getApiService()
                .profile(tokenBody);
        call.enqueue(new Callback<ResponseProfile>() {
                         @Override
                         public void onResponse(Call<ResponseProfile> call, Response<ResponseProfile> response) {
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
                         public void onFailure(Call<ResponseProfile> call, Throwable t) {
                             t.printStackTrace();
                         }
                     }
        );
        return liveData;
    }

    public MutableLiveData<AppResponse> logoutRequest(String token) {
        MutableLiveData<AppResponse> liveData = new MutableLiveData<>();
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);

        Call<ResponseSimple> call = ApiFactory.getApiFactory().getApiService()
                .logout(tokenBody);
        call.enqueue(new Callback<ResponseSimple>() {
                         @Override
                         public void onResponse(Call<ResponseSimple> call, Response<ResponseSimple> response) {
                             int code = response.body().getStatus();

                             switch (code) {
                                 case 200:
                                     liveData.setValue(new AppResponse(token));
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

    public MutableLiveData<AppResponse> galleryRequest(String token, String count, String offset) {
        MutableLiveData<AppResponse> liveData = new MutableLiveData<>();
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody countBody = RequestBody.create(MediaType.parse("text/plain"), count);
        RequestBody offsetBody = RequestBody.create(MediaType.parse("text/plain"), offset);

        Call<ResponseImages> responseCall = ApiFactory.getApiFactory().getApiService()
                .gallery(tokenBody, countBody, offsetBody);
        responseCall.enqueue(new Callback<ResponseImages>() {
                                 @Override
                                 public void onResponse(Call<ResponseImages> call, Response<ResponseImages> response) {
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
                                 public void onFailure(Call<ResponseImages> call, Throwable t) {
                                     t.printStackTrace();
                                 }
                             }
        );
        return liveData;
    }

//    public MutableLiveData<AppResponse> galleryAddRequest(String token, File file) {
//        MutableLiveData<AppResponse> liveData = new MutableLiveData<>();
//        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
//        // TODO: file body
//
//        Call<ResponseSimple> responseCall = ApiFactory.getApiFactory().getApiService()
//                .galleryAdd(tokenBody, fileBody);
//        responseCall.enqueue(new Callback<ResponseSimple>() {
//                                 @Override
//                                 public void onResponse(Call<ResponseSimple> call, Response<ResponseSimple> response) {
//                                     int code = response.body().getStatus();
//
//                                     switch (code) {
//                                         case 200:
//                                             liveData.setValue(new AppResponse(response.body().getData()));
//                                             break;
//                                         default:
//                                             liveData.setValue(new AppResponse(String.valueOf(code)));
//                                             break;
//                                     }
//                                 }
//
//                                 @Override
//                                 public void onFailure(Call<ResponseSimple> call, Throwable t) {
//                                     t.printStackTrace();
//                                 }
//                             }
//        );
//        return liveData;
//    }

    public MutableLiveData<AppResponse> galleryRemoveRequest(String token, String id) {
        MutableLiveData<AppResponse> liveData = new MutableLiveData<>();
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody idBody = RequestBody.create(MediaType.parse("text/plain"), id);

        Call<ResponseSimple> responseCall = ApiFactory.getApiFactory().getApiService()
                .galleryRemove(tokenBody, idBody);
        responseCall.enqueue(new Callback<ResponseSimple>() {
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

        Call<ResponseSimple> call = ApiFactory.getApiFactory().getApiService()
                .vote(userTokenBody, voteTokenBody, voteBody);
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

    public MutableLiveData<AppResponse> topPhotosRequest(String token, String count, String offset) {
        MutableLiveData<AppResponse> liveData = new MutableLiveData<>();
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody countBody = RequestBody.create(MediaType.parse("text/plain"), count);
        RequestBody offsetBody = RequestBody.create(MediaType.parse("text/plain"), offset);

        Call<ResponseImages> call = ApiFactory.getApiFactory().getApiService()
                .topPhotos(tokenBody, countBody, offsetBody);
        call.enqueue(new Callback<ResponseImages>() {
                         @Override
                         public void onResponse(Call<ResponseImages> call, Response<ResponseImages> response) {
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
                         public void onFailure(Call<ResponseImages> call, Throwable t) {
                             t.printStackTrace();
                         }
                     }
        );
        return liveData;
    }
}
