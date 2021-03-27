package com.emikhalets.voteapp.network

import com.emikhalets.voteapp.network.pojo.*
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("api/register")
    fun register(
            @Part("email") email: RequestBody?,
            @Part("login") login: RequestBody?,
            @Part("password") password: RequestBody?,
            @Part("tester_name") testerName: RequestBody?): Observable<ResponseBase?>?

    @Multipart
    @POST("api/register/email")
    fun checkEmail(@Part("email") email: RequestBody?): Observable<ResponseBase?>?

    @Multipart
    @POST("api/register/login")
    fun checkLogin(@Part("login") login: RequestBody?): Observable<ResponseBase?>?

    @Multipart
    @POST("api/login")
    fun login(
            @Part("login") login: RequestBody?,
            @Part("password") pass: RequestBody?): Observable<ResponseToken?>?

    @Multipart
    @POST("api/logout")
    fun logout(@Part("user_token") userToken: RequestBody?): Observable<ResponseBase?>?

    @Multipart
    @POST("api/token")
    fun token(@Part("user_token") body: RequestBody?): Observable<ResponseBase?>?

    @Multipart
    @POST("api/profile")
    fun profile(@Part("user_token") body: RequestBody?): Observable<ResponseProfile?>?

    @Multipart
    @POST("api/gallery/add")
    fun galleryAdd(
            @Part("user_token") userToken: RequestBody?,
            @Part("file\"; filename=\"image.png\"") image: RequestBody?): Observable<ResponseBase?>?

    @Multipart
    @POST("api/gallery")
    fun gallery(
            @Part("user_token") userToken: RequestBody?,
            @Part("count") count: RequestBody?,
            @Part("offset") offset: RequestBody?): Observable<ResponseImages?>?

    @Multipart
    @POST("api/gallery/remove")
    fun galleryRemove(
            @Part("user_token") userToken: RequestBody?,
            @Part("id") id: RequestBody?): Observable<ResponseBase?>?

    @Multipart
    @POST("api/vote/create")
    fun voteCreate(@Part("user_token") userToken: RequestBody?): Observable<ResponseVoting?>?

    @Multipart
    @POST("api/vote")
    fun vote(
            @Part("user_token") userToken: RequestBody?,
            @Part("vote_token") voteToken: RequestBody?,
            @Part("vote") vote: RequestBody?): Observable<ResponseBase?>?

    @Multipart
    @POST("api/top/photos")
    fun topPhotos(
            @Part("user_token") userToken: RequestBody?,
            @Part("count") count: RequestBody?,
            @Part("offset") offset: RequestBody?): Observable<ResponseImages?>?
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