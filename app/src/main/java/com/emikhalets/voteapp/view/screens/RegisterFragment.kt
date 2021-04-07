package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentAuthRegisterBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.base.HideDrawerFragment
import com.emikhalets.voteapp.viewmodel.RegisterViewModel

class RegisterFragment : HideDrawerFragment(R.layout.fragment_auth_register) {

    private val binding: FragmentAuthRegisterBinding by viewBinding()
    lateinit var viewModel: RegisterViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = injectViewModel(activity().viewModelFactory)
        activity().title = getString(R.string.auth_title)
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
                    binding.progressBar.visibility = View.VISIBLE
                    viewModel.sendRegisterRequest(login, pass) { isSuccess, error ->
                        onRequestComplete(isSuccess, error)
                    }
                }
                RegisterToast.EMPTY_FIELDS -> toast(R.string.app_toast_fill_fields)
                RegisterToast.INVALID_EMAIL -> toast(R.string.app_toast_invalid_email)
                RegisterToast.INVALID_PASS -> toast(R.string.app_toast_invalid_pass)
                RegisterToast.PASS_MISMATCH -> toast(R.string.app_toast_pass_not_confirm)
            }
        }
    }

    private fun onRequestComplete(isSuccess: Boolean, error: String) {
        binding.progressBar.visibility = View.GONE
        if (isSuccess) navigate(R.id.action_authRegister_to_home)
        else toastLong(error)
    }
}