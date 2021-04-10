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
        initListeners()
        if (savedInstanceState == null) onViewLoaded()
    }

    private fun initListeners() {
        viewModel.userExisting.observe(viewLifecycleOwner, {
            setViewState(ViewState.LOADED)
            navigate(R.id.action_start_to_home)
        })

        viewModel.error.observe(viewLifecycleOwner, EventObserver { error ->
            setViewState(ViewState.LOADED)
            toastLong(error)
            navigate(R.id.action_start_to_authLogin)
        })
    }

    private fun onViewLoaded() {
        setViewState(ViewState.LOADING)
        toast(R.string.app_toast_initialization)
        viewModel.checkUserExistingRequest()
    }

    private fun setViewState(state: ViewState) {
        when (state) {
            ViewState.LOADING -> binding.progressBar.animShow()
            ViewState.LOADED -> binding.progressBar.animHide()
        }
    }
}