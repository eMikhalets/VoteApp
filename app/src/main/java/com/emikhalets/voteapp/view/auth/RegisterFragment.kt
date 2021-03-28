package com.emikhalets.voteapp.view.auth

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentAuthRegisterBinding
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.hideKeyboard
import com.emikhalets.voteapp.utils.popBackStack
import com.emikhalets.voteapp.view.base.SecondaryFragment

class RegisterFragment : SecondaryFragment(R.layout.fragment_auth_register) {

    private val binding: FragmentAuthRegisterBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ACTIVITY.title = getString(R.string.auth_title)
        binding.apply {
            btnRegister.setOnClickListener { onRegisterClick() }
        }
    }

    private fun onRegisterClick() {
        hideKeyboard()
        popBackStack(R.id.homeFragment)
    }
}