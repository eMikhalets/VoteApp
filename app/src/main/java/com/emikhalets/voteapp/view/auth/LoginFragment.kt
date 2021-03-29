package com.emikhalets.voteapp.view.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentAuthLoginBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.base.AuthFragment
import com.emikhalets.voteapp.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class LoginFragment : AuthFragment(R.layout.fragment_auth_login) {

    private val binding: FragmentAuthLoginBinding by viewBinding()
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ACTIVITY.title = getString(R.string.auth_title)
        binding.apply {
            btnLogin.setOnClickListener { onLoginClick() }
            btnRegister.setOnClickListener { onRegisterClick() }
        }
    }

    private fun onLoginClick() {
        hideKeyboard()
        val login = binding.inputLogin.text.toString()
        val pass = binding.inputPass.text.toString()

        if (login.isNotEmpty() && pass.isNotEmpty()) {
            viewModel.sendLoginRequest(login, pass) {
                lifecycleScope.launch {
                    popBackStack(R.id.homeFragment)
                }
            }
        } else {
            toast(getString(R.string.app_toast_fill_fields))
        }
    }

    private fun onRegisterClick() {
        hideKeyboard()
        navigate(R.id.action_authLogin_to_authRegister)
    }
}