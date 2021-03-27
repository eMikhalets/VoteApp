package com.emikhalets.voteapp.view.auth

import android.app.Activity
import android.content.Context
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
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentLoginBinding
import com.emikhalets.voteapp.utils.Const
import com.emikhalets.voteapp.view.login.LoginViewModel

class LoginFragment : Fragment() {

    private var viewModel: LoginViewModel? = null
    private var binding: FragmentLoginBinding? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel!!.throwable.observe(viewLifecycleOwner, { message: String? -> errorObserver(message) })
        viewModel!!.userToken.observe(viewLifecycleOwner, { userToken: String? -> userTokenObserver(userToken) })
        viewModel!!.errorMessage.observe(viewLifecycleOwner, { message: String? -> errorObserver(message) })
        binding!!.btnLogin.setOnClickListener { v: View? -> onLoginClick() }
        binding!!.btnRegistration.setOnClickListener { v: View? -> onRegisterClick() }
        binding!!.etLogin.addTextChangedListener(onLoginTextChanged())
        binding!!.etPass.addTextChangedListener(onPassTextChanged())
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun userTokenObserver(userToken: String?) {
        saveUserToken(userToken)
        navigateToHome(userToken)
    }

    private fun errorObserver(message: String?) {
        binding!!.textErrorMessage.text = message
    }

    private fun onLoginClick() {
        hideKeyboard()
        val login = binding!!.etLogin.text.toString().trim { it <= ' ' }
        val pass = binding!!.etPass.text.toString().trim { it <= ' ' }
        if (login.isEmpty()) binding!!.tilLogin.error = getString(R.string.login_toast_empty_login)
        if (pass.isEmpty()) binding!!.tilPass.error = getString(R.string.login_toast_empty_pass)
        if (!login.isEmpty() && !pass.isEmpty()) viewModel!!.loginRequest(login, pass)
    }

    private fun onRegisterClick() {
        hideKeyboard()
        navigateToRegister()
    }

    private fun onLoginTextChanged(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                binding!!.tilLogin.error = null
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

    // Navigate to home (news) view
    private fun navigateToHome(userToken: String?) {
        val args = Bundle()
        args.putString(Const.ARGS_TOKEN, userToken)
        Navigation.findNavController(binding!!.root)
                .navigate(R.id.action_loginFragment_to_homeFragment, args)
    }

    // Navigate to register view
    private fun navigateToRegister() {
        Navigation.findNavController(binding!!.root)
                .navigate(R.id.action_loginFragment_to_registerFragment)
    }

    // Save token to SharedPreferences
    fun saveUserToken(token: String?) {
        val sp = requireActivity().getSharedPreferences(
                Const.SHARED_FILE, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString(Const.SHARED_TOKEN, token)
        editor.apply()
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