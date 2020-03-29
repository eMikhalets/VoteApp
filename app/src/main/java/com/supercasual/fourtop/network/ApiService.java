package com.supercasual.fourtop.network;

import com.supercasual.fourtop.network.pojo.*;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @Multipart
    @POST("api/register")
    Call<Register> register(
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("tester_name") RequestBody testerName);

    @Multipart
    @POST("api/login")
    Call<Login> login(
            @Part("login") RequestBody login,
            @Part("password") RequestBody password);

    @Multipart
    @POST("api/logout")
    Call<Logout> logout(
            @Part("user_token") RequestBody userToken);

    //gallery

    @POST("api/gallery/add")
    Call<GalleryAdd> galleryAdd(
            @Part("user_token") RequestBody userToken,
            @Part("file") RequestBody file);

    @POST("api/gallery")
    Call<Gallery> gallery(
            @Part("user_token") RequestBody userToken,
            @Part("count") RequestBody count,
            @Part("offset") RequestBody offset);

    @POST("api/gallery/remove")
    Call<GalleryRemove> galleryRemove(
            @Part("user_token") RequestBody userToken,
            @Part("id") RequestBody id);

    //vote

    @POST("api/vote/create")
    Call<VoteCreate> voteCreate(
            @Part("user_token") RequestBody userToken);

    @POST("api/vote")
    Call<Vote> vote(
            @Part("user_token") RequestBody userToken,
            @Part("vote_token") RequestBody voteToken,
            @Part("vote") RequestBody vote);

    //top

    @POST("api/top/photos")
    Call<TopPhotos> topPhotos(
            @Part("user_token") RequestBody userToken,
            @Part("count") RequestBody count,
            @Part("offset") RequestBody offset);
}
