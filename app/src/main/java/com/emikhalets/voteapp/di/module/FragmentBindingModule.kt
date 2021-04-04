package com.emikhalets.voteapp.di.module

import com.emikhalets.voteapp.di.scopes.FragmentScoped
import com.emikhalets.voteapp.view.screens.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideHomeFragment(): HomeFragment
}