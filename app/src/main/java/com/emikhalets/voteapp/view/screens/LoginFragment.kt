package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentAuthLoginBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.base.NoDrawerFragment
import com.emikhalets.voteapp.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class LoginFragment : NoDrawerFragment(R.layout.fragment_auth_login) {

    private val binding: FragmentAuthLoginBinding by viewBinding()
    lateinit var viewModel: LoginViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = injectViewModel(ACTIVITY.viewModelFactory)
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
            viewModel.sendLoginRequest(login, pass, { onUserExist() }, { onUserNotExist() })
        } else {
            toast(getString(R.string.app_toast_fill_fields))
        }
    }

    private fun onUserExist() {
        lifecycleScope.launch {
            ACTIVITY.drawer.updateHeader()
            navigate(R.id.homeFragment)
        }
    }

    private fun onUserNotExist() {
        toast(getString(R.string.app_toast_user_not_exist_db))
    }

    private fun onRegisterClick() {
        hideKeyboard()
        navigate(R.id.action_authLogin_to_authRegister)
    }
}