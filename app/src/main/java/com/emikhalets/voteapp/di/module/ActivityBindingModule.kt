package com.emikhalets.voteapp.di.module

import com.emikhalets.voteapp.di.viewmodel.ViewModelModule
import com.emikhalets.voteapp.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [
        FragmentBindingModule::class,
        ViewModelModule::class
    ])
    abstract fun provideMainActivity(): MainActivity
}