package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentAuthLoginBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.base.AuthFragment
import com.emikhalets.voteapp.viewmodel.LoginViewModel

class LoginFragment : AuthFragment<FragmentAuthLoginBinding>(FragmentAuthLoginBinding::inflate) {

    lateinit var viewModel: LoginViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = injectViewModel(activity().viewModelFactory)
        binding.apply {
            btnLogin.setOnClickListener { onLoginClick() }
            btnRegister.setOnClickListener { onRegisterClick() }
        }
    }

    private fun onLoginClick() {
        hideKeyboard()
        val login = binding.inputLogin.text.toString()
        val pass = binding.inputPass.text.toString()
        validateLogIn(login, pass) {
            when (it) {
                LoginToast.SUCCESS -> {
                    binding.progressBar.visibility = View.VISIBLE
                    viewModel.sendLoginRequest(login, pass) { isSuccess, error ->
                        onRequestComplete(isSuccess, error)
                    }
                }
                LoginToast.EMPTY_FIELDS -> toast(R.string.app_toast_fill_fields)
                LoginToast.INVALID_EMAIL -> toast(R.string.app_toast_invalid_email)
                LoginToast.INVALID_PASS -> toast(R.string.app_toast_invalid_pass)
            }
        }
    }

    private fun onRequestComplete(isSuccess: Boolean, error: String) {
        binding.progressBar.visibility = View.GONE
        if (isSuccess) navigate(R.id.action_authLogin_to_home)
        else toastLong(error)
    }

    private fun onRegisterClick() {
        hideKeyboard()
        navigate(R.id.action_authLogin_to_authRegister)
    }
}