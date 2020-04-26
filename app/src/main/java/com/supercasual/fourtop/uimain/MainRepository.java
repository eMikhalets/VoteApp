package com.supercasual.fourtop.uimain;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.squareup.picasso.Picasso;
import com.supercasual.fourtop.network.ApiFactory;
import com.supercasual.fourtop.network.pojo.AppResponse;
import com.supercasual.fourtop.network.pojo.ResponseImages;
import com.supercasual.fourtop.network.pojo.ResponseSimple;
import com.supercasual.fourtop.network.pojo.ResponseVoting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Send Requests:
 * /api/logout
 * /api/gallery
 * /api/gallery/add
 * /api/gallery/remove
 * /api/vote
 * /api/vote/create
 * /api/top/photos
 */
public class MainRepository {

    private String header = "Новости 4TOP";
    private String content = "Идет активная разработка сайта 4top. Представленный вниманию " +
            "Толковый словарь «умных слов» включает в себя специфические слова, неупотребляемые " +
            "широко. Собраны термины из культурологии, психологии, медицины, мифологии, " +
            "политологии и других научных и совсем ненаучных сфер. Также здесь попадаются " +
            "современные неологизмы и свежезаимствованные из других языков слова, еще не имеющие " +
            "твердо устоявшегося определения. Все описания максимально кратки и " +
            "конкретизированы. Если какие-то из них идут в разрез с устоявшимися " +
            "энциклопедическими трактовками, то это доказательство более широкого смыслового " +
            "значения слова. Глоссарий постоянно переосмысливается и пополняется новыми " +
            "определениями. Предупреждение: Чрезмерное использование особо умного лексикона в " +
            "обыденной речи грозит непониманием со стороны собеседников. Из интервью 2009 года:" +
            " Глоссарий – развивающийся раздел. «Научный вклад». :) Свое начало он берет давно," +
            " еще с записных книжек, куда выписывались непонятные, но красивые или звучные " +
            "слова, в основном специальные термины, значение которых, порой просто невозможно " +
            "было найти в словарях, а толкование приходилось искать в контексте его " +
            "употребления. Интересно, что и сейчас некоторые из слов Глоссария не встречаются" +
            " ни в бумажных словарях, ни в Википедии! полностью >>> Как из копеек составляются " +
            "рубли, так и из крупинок прочитанного составляется знание. Владимир Иванович Даль";

    public LiveData<List<String>> getHomeData() {
        MutableLiveData<List<String>> liveData = new MutableLiveData<>();
        List<String> data = new ArrayList<>();
        data.add(header);
        data.add(content);
        liveData.setValue(data);
        return liveData;
    }

    public LiveData<AppResponse> logoutRequest(String token) {
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

    public LiveData<AppResponse> galleryRequest(String token, String count, String offset) {
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

//    public LiveData<AppResponse> galleryAddRequest(String token, File file) {
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

    public LiveData<AppResponse> galleryRemoveRequest(String token, String id) {
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

    public LiveData<AppResponse> voteCreateRequest(String token) {
        MutableLiveData<AppResponse> liveData = new MutableLiveData<>();
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);

        Call<ResponseVoting> call = ApiFactory.getApiFactory().getApiService().voteCreate(tokenBody);
        call.enqueue(new Callback<ResponseVoting>() {
                         @Override
                         public void onResponse(Call<ResponseVoting> call, Response<ResponseVoting> response) {
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
                         public void onFailure(Call<ResponseVoting> call, Throwable t) {
                             t.printStackTrace();
                         }
                     }
        );
        return liveData;
    }

    public LiveData<AppResponse> voteRequest(String userToken, String voteToken, String vote) {
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

    public LiveData<AppResponse> topPhotosRequest(String token, String count, String offset) {
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
