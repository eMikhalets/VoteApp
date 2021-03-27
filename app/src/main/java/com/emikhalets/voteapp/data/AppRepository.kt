package com.emikhalets.voteapp.data

import com.emikhalets.voteapp.network.ApiFactory
import com.emikhalets.voteapp.network.ApiService
import com.emikhalets.voteapp.network.pojo.*
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

class AppRepository private constructor() {
    private val apiService: ApiService
    fun registerRequest(email: String?, login: String?,
                        password: String?, name: String?): Observable<ResponseBase> {
        val emailBody = RequestBody.create(MediaType.parse("text/plain"), email)
        val loginBody = RequestBody.create(MediaType.parse("text/plain"), login)
        val passBody = RequestBody.create(MediaType.parse("text/plain"), password)
        val nameBody = RequestBody.create(MediaType.parse("text/plain"), name)
        return apiService.register(emailBody, loginBody, passBody, nameBody)
    }

    fun checkEmailRequest(email: String?): Observable<ResponseBase> {
        val emailBody = RequestBody.create(MediaType.parse("text/plain"), email)
        return apiService.checkEmail(emailBody)
    }

    fun checkLoginRequest(login: String?): Observable<ResponseBase> {
        val loginBody = RequestBody.create(MediaType.parse("text/plain"), login)
        return apiService.checkLogin(loginBody)
    }

    fun loginRequest(login: String?, password: String?): Observable<ResponseToken> {
        val loginBody = RequestBody.create(MediaType.parse("text/plain"), login)
        val passBody = RequestBody.create(MediaType.parse("text/plain"), password)
        return apiService.login(loginBody, passBody)
    }

    fun logoutRequest(token: String?): Observable<ResponseBase> {
        val tokenBody = RequestBody.create(MediaType.parse("text/plain"), token)
        return apiService.logout(tokenBody)
    }

    fun tokenRequest(token: String?): Observable<ResponseBase> {
        val tokenBody = RequestBody.create(MediaType.parse("text/plain"), token)
        return apiService.token(tokenBody)
    }

    fun profileRequest(token: String?): Observable<ResponseProfile> {
        val tokenBody = RequestBody.create(MediaType.parse("text/plain"), token)
        return apiService.profile(tokenBody)
    }

    fun galleryAddRequest(token: String?, file: File?): Observable<ResponseBase> {
        val tokenBody = RequestBody.create(MediaType.parse("text/plain"), token)
        val fileBody = RequestBody.create(MediaType.parse("image/*"), file)
        return apiService.galleryAdd(tokenBody, fileBody)
    }

    fun galleryRequest(token: String?, count: String?, offset: String?): Observable<ResponseImages> {
        val tokenBody = RequestBody.create(MediaType.parse("text/plain"), token)
        val countBody = RequestBody.create(MediaType.parse("text/plain"), count)
        val offsetBody = RequestBody.create(MediaType.parse("text/plain"), offset)
        return apiService.gallery(tokenBody, countBody, offsetBody)
    }

    fun galleryRemoveRequest(token: String?, id: String?): Observable<ResponseBase> {
        val tokenBody = RequestBody.create(MediaType.parse("text/plain"), token)
        val idBody = RequestBody.create(MediaType.parse("text/plain"), id)
        return apiService.galleryRemove(tokenBody, idBody)
    }

    fun voteCreateRequest(token: String?): Observable<ResponseVoting> {
        val tokenBody = RequestBody.create(MediaType.parse("text/plain"), token)
        return apiService.voteCreate(tokenBody)
    }

    fun voteRequest(userToken: String?, voteToken: String?, vote: String?): Observable<ResponseBase> {
        val userTokenBody = RequestBody.create(MediaType.parse("text/plain"), userToken)
        val voteTokenBody = RequestBody.create(MediaType.parse("text/plain"), voteToken)
        val voteBody = RequestBody.create(MediaType.parse("text/plain"), vote)
        return apiService.vote(userTokenBody, voteTokenBody, voteBody)
    }

    fun topPhotosRequest(token: String?, count: String?, offset: String?): Observable<ResponseImages> {
        val tokenBody = RequestBody.create(MediaType.parse("text/plain"), token)
        val countBody = RequestBody.create(MediaType.parse("text/plain"), count)
        val offsetBody = RequestBody.create(MediaType.parse("text/plain"), offset)
        return apiService.topPhotos(tokenBody, countBody, offsetBody)
    }

    fun topUsersRequest(token: String?): Observable<ResponseImages>? {
        return null
    }

    companion object {
        private var instance: AppRepository? = null
        @JvmStatic
        @Synchronized
        fun get(): AppRepository? {
            if (instance == null) instance = AppRepository()
            return instance
        }
    }

    init {
        apiService = ApiFactory.apiFactory.apiService
    }
}