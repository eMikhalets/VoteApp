package com.emikhalets.voteapp.view.auth

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentAuthRegisterBinding
import com.emikhalets.voteapp.model.firebase.sendRegisterToFirebase
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.hideKeyboard
import com.emikhalets.voteapp.utils.toast
import com.emikhalets.voteapp.view.base.AuthFragment

class RegisterFragment : AuthFragment(R.layout.fragment_auth_register) {

    private val binding: FragmentAuthRegisterBinding by viewBinding()
    private lateinit var login: String
    private lateinit var pass: String
    private lateinit var passConf: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ACTIVITY.title = getString(R.string.auth_title)
        binding.btnRegister.setOnClickListener { onRegisterClick() }
    }

    private fun onRegisterClick() {
        hideKeyboard()
        login = binding.inputLogin.text.toString()
        pass = binding.inputPass.text.toString()
        passConf = binding.inputPassConf.text.toString()
        checkFillFields()
    }

    private fun checkFillFields() {
        if (login.isNotEmpty() && pass.isNotEmpty() && passConf.isNotEmpty()) checkPasswords()
        else toast(getString(R.string.app_toast_fill_fields))
    }

    private fun checkPasswords() {
        if (pass == passConf) sendRegisterToFirebase(login, pass)
        else toast(getString(R.string.app_toast_pass_not_confirm))
    }
}