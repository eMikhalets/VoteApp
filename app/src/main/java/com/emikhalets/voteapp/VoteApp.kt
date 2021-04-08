package com.emikhalets.voteapp

import com.emikhalets.voteapp.di.DaggerAppComponent
import com.emikhalets.voteapp.utils.initLogger
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class VoteApp : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        initLogger()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }
}