package com.emikhalets.voteapp.di

import android.app.Application
import com.emikhalets.voteapp.VoteApp
import com.emikhalets.voteapp.di.module.ActivityBindingModule
import com.emikhalets.voteapp.di.module.DataModule
import com.emikhalets.voteapp.di.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    ActivityBindingModule::class,
    ViewModelModule::class,
    DataModule::class
])
interface AppComponent : AndroidInjector<VoteApp> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}