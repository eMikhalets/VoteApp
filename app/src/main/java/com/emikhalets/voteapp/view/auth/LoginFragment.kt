package com.emikhalets.voteapp.view.auth

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentAuthLoginBinding
import com.emikhalets.voteapp.model.firebase.sendLoginToFirebase
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.hideKeyboard
import com.emikhalets.voteapp.utils.navigate
import com.emikhalets.voteapp.utils.toast
import com.emikhalets.voteapp.view.base.AuthFragment

class LoginFragment : AuthFragment(R.layout.fragment_auth_login) {

    private val binding: FragmentAuthLoginBinding by viewBinding()
    private lateinit var login: String
    private lateinit var pass: String

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
        login = binding.inputLogin.text.toString()
        pass = binding.inputPass.text.toString()
        checkFillFields()
    }

    private fun checkFillFields() {
        if (login.isNotEmpty() && pass.isNotEmpty()) sendLoginToFirebase(login, pass)
        else toast(getString(R.string.app_toast_fill_fields))
    }

    private fun onRegisterClick() {
        hideKeyboard()
        navigate(R.id.action_authLogin_to_authRegister)
    }
}