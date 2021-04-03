package com.emikhalets.voteapp.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emikhalets.voteapp.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(viewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StartViewModel::class)
    abstract fun bindStartViewModel(viewModel: StartViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopImagesViewModel::class)
    abstract fun bindTopImagesViewModel(viewModel: TopImagesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopUsersViewModel::class)
    abstract fun bindTopUsersViewModel(viewModel: TopUsersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserImagesViewModel::class)
    abstract fun bindUserImagesViewModel(viewModel: UserImagesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VotingViewModel::class)
    abstract fun bindVotingViewModel(viewModel: VotingViewModel): ViewModel
}