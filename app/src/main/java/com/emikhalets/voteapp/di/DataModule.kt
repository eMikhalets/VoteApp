package com.emikhalets.voteapp.di

import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideDatabaseRepository(): FirebaseDatabaseRepository {
        return FirebaseDatabaseRepository()
    }
}