package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.utils.USER_ID
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
        viewModel.sendCheckUserExistRequest {
            lifecycleScope.launch {
                if (it && USER_ID.isNotEmpty()) navigate(R.id.action_start_to_home)
                else navigate(R.id.action_start_to_authLogin)
            }
        }
    }
}