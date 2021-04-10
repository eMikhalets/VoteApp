package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentAuthLoginBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.base.AuthFragment
import com.emikhalets.voteapp.viewmodel.LoginViewModel

class LoginFragment : AuthFragment<FragmentAuthLoginBinding>(FragmentAuthLoginBinding::inflate) {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        viewModel.loginState.observe(viewLifecycleOwner, {
            setViewState(ViewState.LOADED)
            navigate(R.id.action_authLogin_to_home)
        })

        viewModel.error.observe(viewLifecycleOwner, EventObserver { message ->
            setViewState(ViewState.LOADED)
            toastLong(message)
        })

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
                    setViewState(ViewState.LOADING)
                    viewModel.sendLoginRequest(login, pass)
                }
                LoginToast.EMPTY_FIELDS -> toast(R.string.app_toast_fill_fields)
                LoginToast.INVALID_EMAIL -> toast(R.string.app_toast_invalid_email)
                LoginToast.INVALID_PASS -> toast(R.string.app_toast_invalid_pass)
            }
        }
    }

    private fun onRegisterClick() {
        hideKeyboard()
        navigate(R.id.action_authLogin_to_authRegister)
    }

    private fun setViewState(state: ViewState) {
        when (state) {
            ViewState.LOADING -> binding.progressBar.animShow()
            ViewState.LOADED -> binding.progressBar.animHide()
        }
    }
}