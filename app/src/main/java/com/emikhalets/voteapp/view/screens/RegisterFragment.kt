package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentAuthRegisterBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.base.AuthFragment
import com.emikhalets.voteapp.viewmodel.RegisterViewModel

class RegisterFragment : AuthFragment<FragmentAuthRegisterBinding>(FragmentAuthRegisterBinding::inflate) {

    lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        binding.btnRegister.setOnClickListener { onRegisterClick() }
    }

    private fun initListeners() {
        viewModel.registerState.observe(viewLifecycleOwner, {
            setViewState(ViewState.LOADED)
            navigate(R.id.action_authRegister_to_home)
        })

        viewModel.error.observe(viewLifecycleOwner, EventObserver { message ->
            setViewState(ViewState.LOADED)
            toastLong(message)
        })

        binding.btnRegister.setOnClickListener { onRegisterClick() }
    }

    private fun onRegisterClick() {
        hideKeyboard()
        val login = binding.inputLogin.text.toString()
        val pass = binding.inputPass.text.toString()
        val passConf = binding.inputPassConf.text.toString()
        validateRegister(login, pass, passConf) {
            when (it) {
                RegisterToast.SUCCESS -> {
                    setViewState(ViewState.LOADING)
                    viewModel.sendRegisterRequest(login, pass)
                }
                RegisterToast.EMPTY_FIELDS -> toast(R.string.app_toast_fill_fields)
                RegisterToast.INVALID_EMAIL -> toast(R.string.app_toast_invalid_email)
                RegisterToast.INVALID_PASS -> toast(R.string.app_toast_invalid_pass)
                RegisterToast.PASS_MISMATCH -> toast(R.string.app_toast_pass_not_confirm)
            }
        }
    }

    private fun setViewState(state: ViewState) {
        when (state) {
            ViewState.LOADING -> binding.progressBar.animShow()
            ViewState.LOADED -> binding.progressBar.animHide()
        }
    }
}