package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.navigate
import com.emikhalets.voteapp.view.base.NoDrawerFragment
import com.emikhalets.voteapp.viewmodel.StartViewModel
import kotlinx.coroutines.launch

class StartFragment : NoDrawerFragment(R.layout.fragment_start) {

    private val viewModel: StartViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) onViewLoaded()
    }

    private fun onViewLoaded() {
        viewModel.sendLoadUserDataRequest({ onUserExist() }, { onUserNotExist() })
    }

    private fun onUserExist() {
        lifecycleScope.launch {
            ACTIVITY.drawer.updateHeader()
            navigate(R.id.action_start_to_home)
        }
    }

    private fun onUserNotExist() {
        lifecycleScope.launch {
            navigate(R.id.action_start_to_authLogin)
        }
    }
}