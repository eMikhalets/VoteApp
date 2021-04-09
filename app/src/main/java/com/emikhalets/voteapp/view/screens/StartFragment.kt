package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentStartBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.base.AuthFragment
import com.emikhalets.voteapp.viewmodel.StartViewModel

class StartFragment : AuthFragment<FragmentStartBinding>(FragmentStartBinding::inflate) {

    private lateinit var viewModel: StartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) onViewLoaded()
    }

    private fun onViewLoaded() {
        setViewState(ViewState.LOADING)
        toast(R.string.app_toast_initialization)
        viewModel.sendLoadUserDataRequest { isExist, error ->
            setViewState(ViewState.LOADED)
            if (isExist) {
                navigate(R.id.action_start_to_home)
            } else {
                toastLong(error)
                navigate(R.id.action_start_to_authLogin)
            }
        }
    }

    private fun setViewState(state: ViewState) {
        when (state) {
            ViewState.LOADING -> binding.progressBar.animShow()
            ViewState.LOADED -> binding.progressBar.animHide()
        }
    }
}