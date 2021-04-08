package com.emikhalets.voteapp.view.base

import com.emikhalets.voteapp.di.viewmodel.ViewModelFactory
import dagger.android.support.DaggerDialogFragment
import javax.inject.Inject

abstract class BaseDialog : DaggerDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
}