package com.emikhalets.voteapp.view.register

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.emikhalets.voteapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private var binding: FragmentRegisterBinding? = null
    private var viewModel: RegisterViewModel? = null
    private var isPassConfirm = false
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        viewModel!!.throwable.observe(viewLifecycleOwner, { message: String? -> errorObserver(message) })
        viewModel!!.errorMessage.observe(viewLifecycleOwner, { message: String? -> errorObserver(message) })
        viewModel!!.liveDataEmail.observe(viewLifecycleOwner, { status: Int -> checkEmailObserver(status) })
        viewModel!!.liveDataLogin.observe(viewLifecycleOwner, { status: Int -> checkLoginObserver(status) })
        viewModel!!.liveDataRegister.observe(viewLifecycleOwner, { status: Int -> registerObserver(status) })
        binding!!.btnRegister.setOnClickListener { v: View? -> onRegisterClick() }
        binding!!.etLogin.addTextChangedListener(onLoginTextChanged())
        binding!!.etEmail.addTextChangedListener(onEmailTextChanged())
        binding!!.etPass.addTextChangedListener(onPassTextChanged())
        binding!!.etConfirmPass.addTextChangedListener(onConfPassTextChanged())
        binding!!.etNickname.addTextChangedListener(onNameTextChanged())
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun registerObserver(status: Int) {
        backToLogin()
    }

    private fun checkEmailObserver(status: Int) {
        if (status == 200) binding!!.tilEmail.error = "Почта занята"
    }

    private fun checkLoginObserver(status: Int) {
        if (status == 200) binding!!.tilLogin.error = "Почта занята"
    }

    private fun errorObserver(message: String?) {
        if (message!!.contains("Email")) {
            binding!!.tilEmail.error = message
        } else if (message.contains("Login")) {
            binding!!.tilLogin.error = message
        } else {
            binding!!.textErrorMessage.text = message
        }
    }

    private fun onRegisterClick() {
        hideKeyboard()
        val email = binding!!.etEmail.text.toString().trim { it <= ' ' }
        val login = binding!!.etLogin.text.toString().trim { it <= ' ' }
        val pass = binding!!.etPass.text.toString().trim { it <= ' ' }
        val passConf = binding!!.etConfirmPass.text.toString().trim { it <= ' ' }
        val name = binding!!.etNickname.text.toString().trim { it <= ' ' }
        if (email.isEmpty()) binding!!.tilLogin.error = "Введите почту"
        if (email.isEmpty()) binding!!.tilLogin.error = "Введите логин"
        if (email.isEmpty()) binding!!.tilPass.error = "Введите пароль"
        if (email.isEmpty()) binding!!.tilConfirmPass.error = "Введите пароль"
        if (email.isEmpty()) binding!!.tilNickname.error = "Введите никнейм"
        if (!pass.isEmpty() && !passConf.isEmpty()) {
            if (pass == passConf) {
                isPassConfirm = true
            } else {
                isPassConfirm = false
                binding!!.tilConfirmPass.error = "Пароли не совпадают"
            }
        }
        if (!email.isEmpty() && !login.isEmpty() && !pass.isEmpty() && !name.isEmpty() && isPassConfirm) {
            viewModel!!.checkEmail(email)
            viewModel!!.checkLogin(login)
            viewModel!!.register(email, login, pass, name)
        }
    }

    // Field's text change listeners
    private fun onLoginTextChanged(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                binding!!.tilLogin.error = null
            }
        }
    }

    private fun onEmailTextChanged(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                binding!!.tilEmail.error = null
            }
        }
    }

    private fun onPassTextChanged(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                binding!!.tilPass.error = null
            }
        }
    }

    private fun onConfPassTextChanged(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                binding!!.tilConfirmPass.error = null
            }
        }
    }

    private fun onNameTextChanged(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                binding!!.tilNickname.error = null
            }
        }
    }

    private fun backToLogin() {
        Navigation.findNavController(binding!!.root).popBackStack()
    }

    fun hideKeyboard() {
        if (requireActivity().currentFocus != null) {
            val imm = requireActivity()
                    .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireActivity().currentFocus
                    .getWindowToken(), 0)
        }
    }
}