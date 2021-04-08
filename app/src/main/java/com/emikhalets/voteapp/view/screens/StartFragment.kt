package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentStartBinding
import com.emikhalets.voteapp.utils.injectViewModel
import com.emikhalets.voteapp.utils.navigate
import com.emikhalets.voteapp.utils.toastLong
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
        viewModel.sendLoadUserDataRequest { isExist, error ->
            onRequestComplete(isExist, error)
        }
    }

    private fun onRequestComplete(isExist: Boolean, error: String) {
        if (isExist) {
            navigate(R.id.action_start_to_home)
        } else {
            toastLong(error)
            navigate(R.id.action_start_to_authLogin)
        }
    }
}