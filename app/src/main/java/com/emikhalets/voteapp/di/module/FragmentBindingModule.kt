package com.emikhalets.voteapp.di.module

import com.emikhalets.voteapp.view.screens.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    abstract fun provideStartFragment(): StartFragment

    @ContributesAndroidInjector
    abstract fun provideLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun provideRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector
    abstract fun provideHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun provideProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun provideUserImagesFragment(): UserImagesFragment

    @ContributesAndroidInjector
    abstract fun provideVotingFragment(): VotingFragment

    @ContributesAndroidInjector
    abstract fun provideTopImagesFragment(): TopImagesFragment

    @ContributesAndroidInjector
    abstract fun provideTopUsersFragment(): TopUsersFragment

    @ContributesAndroidInjector
    abstract fun provideImageFragment(): ImageFragment

    @ContributesAndroidInjector
    abstract fun provideChangeNameDialog(): ChangeNameDialog

    @ContributesAndroidInjector
    abstract fun provideChangePassDialog(): ChangePassDialog

    @ContributesAndroidInjector
    abstract fun provideDeleteImageDialog(): DeleteImageDialog

    @ContributesAndroidInjector
    abstract fun provideResetPassDialog(): ResetPassDialog
}