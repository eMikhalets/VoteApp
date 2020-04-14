package com.supercasual.fourtop.network;

import com.supercasual.fourtop.network.pojo.ApiResponse;
import com.supercasual.fourtop.network.pojo.ImagesResponse;
import com.supercasual.fourtop.network.pojo.TokenResponse;
import com.supercasual.fourtop.network.pojo.VoteResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @Multipart
    @POST("api/register")
    Call<ApiResponse> register(
            @Part("email") RequestBody email,
            @Part("login") RequestBody login,
            @Part("password") RequestBody password,
            @Part("tester_name") RequestBody testerName);

    @Multipart
    @POST("api/register/email")
    Call<ApiResponse> registerEmail(@Part("email") RequestBody email);

    @Multipart
    @POST("api/register/login")
    Call<ApiResponse> registerLogin(@Part("login") RequestBody login);

    @Multipart
    @POST("api/login")
    Call<TokenResponse> login(
            @Part("login") RequestBody login,
            @Part("password") RequestBody pass);

    @Multipart
    @POST("api/logout")
    Call<ApiResponse> logout(@Part("user_token") RequestBody userToken);

    @Multipart
    @POST("api/token")
    Call<ApiResponse> token(@Part("user_token") RequestBody body);


    @Multipart
    @POST("api/gallery/add")
    Call<ApiResponse> galleryAdd(
            @Part("user_token") RequestBody userToken,
            @Part("file") RequestBody file);

    @Multipart
    @POST("api/gallery")
    Call<ImagesResponse> gallery(
            @Part("user_token") RequestBody userToken,
            @Part("count") RequestBody count,
            @Part("offset") RequestBody offset);

    @Multipart
    @POST("api/gallery/remove")
    Call<ApiResponse> galleryRemove(
            @Part("user_token") RequestBody userToken,
            @Part("id") RequestBody id);


    @Multipart
    @POST("api/vote/create")
    Call<VoteResponse> voteCreate(@Part("user_token") RequestBody userToken);

    @Multipart
    @POST("api/vote")
    Call<ApiResponse> vote(
            @Part("user_token") RequestBody userToken,
            @Part("vote_token") RequestBody voteToken,
            @Part("vote") RequestBody vote);

    @Multipart
    @POST("api/top/photos")
    Call<ImagesResponse> topPhotos(
            @Part("user_token") RequestBody userToken,
            @Part("count") RequestBody count,
            @Part("offset") RequestBody offset);

}
