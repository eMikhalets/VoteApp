package com.emikhalets.voteapp.network

import com.emikhalets.voteapp.utils.Const
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiFactory private constructor() {
//    val apiService: ApiService
//        get() = retrofit.create(ApiService::class.java)
//
//    companion object {
//        @get:Synchronized
//        var apiFactory: ApiFactory? = null
//            get() {
//                if (field == null) {
//                    field = ApiFactory()
//                }
//                return field
//            }
//            private set
////        private var retrofit: Retrofit
//    }
//
//    init {
//        retrofit = Retrofit.Builder()
//                .baseUrl(Const.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build()
//    }
}