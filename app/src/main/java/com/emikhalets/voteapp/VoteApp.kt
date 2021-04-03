package com.emikhalets.voteapp

import com.emikhalets.voteapp.di.AppComponent
import com.emikhalets.voteapp.di.DaggerAppComponent
import com.emikhalets.voteapp.utils.initLogger
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class VoteApp : DaggerApplication() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initLogger()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent.builder().application(this).build()
        return appComponent
    }
}