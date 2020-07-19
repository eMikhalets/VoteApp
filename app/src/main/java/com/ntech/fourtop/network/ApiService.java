package com.ntech.fourtop.network;

import com.ntech.fourtop.network.pojo.ResponseBase;
import com.ntech.fourtop.network.pojo.ResponseImages;
import com.ntech.fourtop.network.pojo.ResponseProfile;
import com.ntech.fourtop.network.pojo.ResponseToken;
import com.ntech.fourtop.network.pojo.ResponseVoting;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @Multipart
    @POST("api/register")
    Observable<ResponseBase> register(
            @Part("email") RequestBody email,
            @Part("login") RequestBody login,
            @Part("password") RequestBody password,
            @Part("tester_name") RequestBody testerName);

    @Multipart
    @POST("api/register/email")
    Observable<ResponseBase> checkEmail(@Part("email") RequestBody email);

    @Multipart
    @POST("api/register/login")
    Observable<ResponseBase> checkLogin(@Part("login") RequestBody login);

    @Multipart
    @POST("api/login")
    Observable<ResponseToken> login(
            @Part("login") RequestBody login,
            @Part("password") RequestBody pass);

    @Multipart
    @POST("api/logout")
    Observable<ResponseBase> logout(@Part("user_token") RequestBody userToken);

    @Multipart
    @POST("api/token")
    Observable<ResponseBase> token(@Part("user_token") RequestBody body);

    @Multipart
    @POST("api/profile")
    Observable<ResponseProfile> profile(@Part("user_token") RequestBody body);


    @Multipart
    @POST("api/gallery/add")
    Observable<ResponseBase> galleryAdd(
            @Part("user_token") RequestBody userToken,
            @Part("file\"; filename=\"image.png\"") RequestBody image);

    @Multipart
    @POST("api/gallery")
    Observable<ResponseImages> gallery(
            @Part("user_token") RequestBody userToken,
            @Part("count") RequestBody count,
            @Part("offset") RequestBody offset);

    @Multipart
    @POST("api/gallery/remove")
    Observable<ResponseBase> galleryRemove(
            @Part("user_token") RequestBody userToken,
            @Part("id") RequestBody id);


    @Multipart
    @POST("api/vote/create")
    Observable<ResponseVoting> voteCreate(@Part("user_token") RequestBody userToken);

    @Multipart
    @POST("api/vote")
    Observable<ResponseBase> vote(
            @Part("user_token") RequestBody userToken,
            @Part("vote_token") RequestBody voteToken,
            @Part("vote") RequestBody vote);

    @Multipart
    @POST("api/top/photos")
    Observable<ResponseImages> topPhotos(
            @Part("user_token") RequestBody userToken,
            @Part("count") RequestBody count,
            @Part("offset") RequestBody offset);

    /**
     * Этот запрос еще не реализован на сервере
     */
//    @Multipart
//    @POST("api/top/users")
//    Observable<ResponseUsers> topUsers(
//            @Part("user_token") RequestBody userToken,
//            @Part("count") RequestBody count,
//            @Part("offset") RequestBody offset);

}
