package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.utils.activity
import com.emikhalets.voteapp.utils.injectViewModel
import com.emikhalets.voteapp.utils.navigate
import com.emikhalets.voteapp.utils.toastLong
import com.emikhalets.voteapp.view.base.HideDrawerFragment
import com.emikhalets.voteapp.viewmodel.StartViewModel

class StartFragment : HideDrawerFragment(R.layout.fragment_start) {

    lateinit var viewModel: StartViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = injectViewModel(activity().viewModelFactory)
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
            navigate(R.id.action_start_to_authLogin)
            toastLong(error)
        }
    }
}