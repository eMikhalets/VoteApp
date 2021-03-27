package com.emikhalets.voteapp.view.logo

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentLogoBinding
import com.emikhalets.voteapp.utils.Const

class LogoFragment : Fragment() {
    private var viewModel: LogoViewModel? = null
    private var binding: FragmentLogoBinding? = null
    private var userToken: String? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLogoBinding.inflate(inflater, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(LogoViewModel::class.java)
        viewModel!!.throwable.observe(viewLifecycleOwner, { throwable: String? -> throwableObserver(throwable) })
        viewModel!!.errorMessage.observe(viewLifecycleOwner, { throwable: String? -> errorMessageObserver(throwable) })
        viewModel!!.liveDataResponse.observe(viewLifecycleOwner, { status: Int -> responseObserver(status) })
    }

    override fun onResume() {
        super.onResume()
        if (userToken == null) loadUserToken()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun throwableObserver(throwable: String?) {
        binding!!.textError.text = throwable
    }

    private fun errorMessageObserver(throwable: String?) {
        navigateToLogin()
    }

    private fun responseObserver(status: Int) {
        navigateToHome()
    }

    // Get user token from SharedPreferences.
    // Send token request or navigate to login view
    private fun loadUserToken() {
        val sp = requireActivity().getSharedPreferences(
                Const.SHARED_FILE, Context.MODE_PRIVATE)
        userToken = sp.getString(Const.SHARED_TOKEN, "")
        if (userToken != null && !userToken!!.isEmpty()) {
            viewModel!!.tokenRequest(userToken)
        } else {
            navigateToLogin()
        }
    }

    // Navigate to home (news) view
    private fun navigateToHome() {
        val args = Bundle()
        args.putString(Const.ARGS_TOKEN, userToken)
        Navigation.findNavController(binding!!.root)
                .navigate(R.id.action_logoFragment_to_homeFragment, args)
    }

    // Navigate to login view
    private fun navigateToLogin() {
        Navigation.findNavController(binding!!.root)
                .navigate(R.id.action_logoFragment_to_loginFragment)
    }
}