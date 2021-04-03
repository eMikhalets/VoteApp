package com.emikhalets.voteapp.di

import com.emikhalets.voteapp.di.scopes.ActivityScoped
import com.emikhalets.voteapp.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun provideMainActivity(): MainActivity
}