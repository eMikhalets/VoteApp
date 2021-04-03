package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentAuthRegisterBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.base.NoDrawerFragment
import com.emikhalets.voteapp.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch

class RegisterFragment : NoDrawerFragment(R.layout.fragment_auth_register) {

    private val binding: FragmentAuthRegisterBinding by viewBinding()
    lateinit var viewModel: RegisterViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = injectViewModel(ACTIVITY.viewModelFactory)
        ACTIVITY.title = getString(R.string.auth_title)
        binding.btnRegister.setOnClickListener { onRegisterClick() }
    }

    private fun onRegisterClick() {
        hideKeyboard()
        val login = binding.inputLogin.text.toString()
        val pass = binding.inputPass.text.toString()
        val passConf = binding.inputPassConf.text.toString()

        if (login.isNotEmpty() && pass.isNotEmpty() && passConf.isNotEmpty()) {
            if (pass == passConf) {
                viewModel.sendRegisterRequest(login, pass) {
                    lifecycleScope.launch {
                        ACTIVITY.drawer.updateHeader()
                        navigate(R.id.homeFragment)
                    }
                }
            } else {
                toast(getString(R.string.app_toast_pass_not_confirm))
            }
        } else {
            toast(getString(R.string.app_toast_fill_fields))
        }
    }
}