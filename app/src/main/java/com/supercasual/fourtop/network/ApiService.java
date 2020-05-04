package com.supercasual.fourtop.network;

import com.supercasual.fourtop.network.pojo.ResponseImages;
import com.supercasual.fourtop.network.pojo.ResponseProfile;
import com.supercasual.fourtop.network.pojo.ResponseSimple;
import com.supercasual.fourtop.network.pojo.ResponseToken;
import com.supercasual.fourtop.network.pojo.ResponseVoting;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @Multipart
    @POST("api/register")
    Call<ResponseSimple> register(
            @Part("email") RequestBody email,
            @Part("login") RequestBody login,
            @Part("password") RequestBody password,
            @Part("tester_name") RequestBody testerName);

    @Multipart
    @POST("api/register/email")
    Call<ResponseSimple> checkEmail(@Part("email") RequestBody email);

    @Multipart
    @POST("api/register/login")
    Call<ResponseSimple> checkLogin(@Part("login") RequestBody login);

    @Multipart
    @POST("api/login")
    Call<ResponseToken> login(
            @Part("login") RequestBody login,
            @Part("password") RequestBody pass);

    @Multipart
    @POST("api/logout")
    Call<ResponseSimple> logout(@Part("user_token") RequestBody userToken);

    @Multipart
    @POST("api/token")
    Call<ResponseSimple> token(@Part("user_token") RequestBody body);

    @Multipart
    @POST("api/profile")
    Call<ResponseProfile> profile(@Part("user_token") RequestBody body);


    @Multipart
    @POST("api/gallery/add")
    Call<ResponseSimple> galleryAdd(
            @Part("user_token") RequestBody userToken,
            @Part("file") MultipartBody.Part file);

    @Multipart
    @POST("api/gallery")
    Call<ResponseImages> gallery(
            @Part("user_token") RequestBody userToken,
            @Part("count") RequestBody count,
            @Part("offset") RequestBody offset);

    @Multipart
    @POST("api/gallery/remove")
    Call<ResponseSimple> galleryRemove(
            @Part("user_token") RequestBody userToken,
            @Part("id") RequestBody id);


    @Multipart
    @POST("api/vote/create")
    Call<ResponseVoting> voteCreate(@Part("user_token") RequestBody userToken);

    @Multipart
    @POST("api/vote")
    Call<ResponseSimple> vote(
            @Part("user_token") RequestBody userToken,
            @Part("vote_token") RequestBody voteToken,
            @Part("vote") RequestBody vote);

    @Multipart
    @POST("api/top/photos")
    Call<ResponseImages> topPhotos(
            @Part("user_token") RequestBody userToken,
            @Part("count") RequestBody count,
            @Part("offset") RequestBody offset);

    /**
     * Этот запрос еще не реализован на сервере
     */
//    @Multipart
//    @POST("api/top/users")
//    Call<ResponseUsers> topUsers(
//            @Part("user_token") RequestBody userToken,
//            @Part("count") RequestBody count,
//            @Part("offset") RequestBody offset);

}
