package com.ntech.fourtop.data;

import com.ntech.fourtop.network.ApiFactory;
import com.ntech.fourtop.network.ApiService;
import com.ntech.fourtop.network.pojo.ResponseBase;
import com.ntech.fourtop.network.pojo.ResponseImages;
import com.ntech.fourtop.network.pojo.ResponseProfile;
import com.ntech.fourtop.network.pojo.ResponseToken;
import com.ntech.fourtop.network.pojo.ResponseVoting;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AppRepository {

    private ApiService apiService;
    private static AppRepository instance;

    private AppRepository() {
        apiService = ApiFactory.getApiFactory().getApiService();
    }

    public static synchronized AppRepository get() {
        if (instance == null) instance = new AppRepository();
        return instance;
    }

    public Observable<ResponseBase> registerRequest(String email, String login,
                                                    String password, String name) {
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody loginBody = RequestBody.create(MediaType.parse("text/plain"), login);
        RequestBody passBody = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        return apiService.register(emailBody, loginBody, passBody, nameBody);
    }

    public Observable<ResponseBase> checkEmailRequest(String email) {
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        return apiService.checkEmail(emailBody);
    }

    public Observable<ResponseBase> checkLoginRequest(String login) {
        RequestBody loginBody = RequestBody.create(MediaType.parse("text/plain"), login);
        return apiService.checkLogin(loginBody);
    }

    public Observable<ResponseToken> loginRequest(String login, String password) {
        RequestBody loginBody = RequestBody.create(MediaType.parse("text/plain"), login);
        RequestBody passBody = RequestBody.create(MediaType.parse("text/plain"), password);
        return apiService.login(loginBody, passBody);
    }

    public Observable<ResponseBase> logoutRequest(String token) {
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        return apiService.logout(tokenBody);
    }

    public Observable<ResponseBase> tokenRequest(String token) {
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        return apiService.token(tokenBody);
    }

    public Observable<ResponseProfile> profileRequest(String token) {
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        return apiService.profile(tokenBody);
    }

    public Observable<ResponseBase> galleryAddRequest(String token, File file) {
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
        return apiService.galleryAdd(tokenBody, fileBody);
    }

    public Observable<ResponseImages> galleryRequest(String token, String count, String offset) {
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody countBody = RequestBody.create(MediaType.parse("text/plain"), count);
        RequestBody offsetBody = RequestBody.create(MediaType.parse("text/plain"), offset);
        return apiService.gallery(tokenBody, countBody, offsetBody);
    }

    public Observable<ResponseBase> galleryRemoveRequest(String token, String id) {
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody idBody = RequestBody.create(MediaType.parse("text/plain"), id);
        return apiService.galleryRemove(tokenBody, idBody);
    }

    public Observable<ResponseVoting> voteCreateRequest(String token) {
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        return apiService.voteCreate(tokenBody);
    }

    public Observable<ResponseBase> voteRequest(String userToken, String voteToken, String vote) {
        RequestBody userTokenBody = RequestBody.create(MediaType.parse("text/plain"), userToken);
        RequestBody voteTokenBody = RequestBody.create(MediaType.parse("text/plain"), voteToken);
        RequestBody voteBody = RequestBody.create(MediaType.parse("text/plain"), vote);
        return apiService.vote(userTokenBody, voteTokenBody, voteBody);
    }

    public Observable<ResponseImages> topPhotosRequest(String token, String count, String offset) {
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody countBody = RequestBody.create(MediaType.parse("text/plain"), count);
        RequestBody offsetBody = RequestBody.create(MediaType.parse("text/plain"), offset);
        return apiService.topPhotos(tokenBody, countBody, offsetBody);
    }

    public Observable<ResponseImages> topUsersRequest(String token) {
        return null;
    }
}
