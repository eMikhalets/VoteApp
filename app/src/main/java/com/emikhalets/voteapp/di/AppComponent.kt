package com.emikhalets.voteapp.di

import android.app.Application
import com.emikhalets.voteapp.VoteApp
import com.emikhalets.voteapp.di.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            (AndroidInjectionModule::class),
            (AndroidSupportInjectionModule::class),
            (AppModule::class),
            (ViewModelModule::class),
            (DataModule::class),
            (ActivityBindingModule::class),
            (FragmentBindingModule::class),
        ]
)
interface AppComponent : AndroidInjector<VoteApp> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }
}