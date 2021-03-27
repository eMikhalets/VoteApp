package com.emikhalets.voteapp.view.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.emikhalets.voteapp.databinding.FragmentProfileBinding
import com.emikhalets.voteapp.network.pojo.DataProfile
import com.emikhalets.voteapp.utils.Const

class ProfileFragment : Fragment() {
    private var viewModel: ProfileViewModel? = null
    private var binding: FragmentProfileBinding? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel!!.logout.observe(viewLifecycleOwner, { status: Int -> logoutObserver(status) })
        viewModel!!.profile.observe(viewLifecycleOwner, { profile: DataProfile? -> profileObserver(profile) })
        viewModel!!.throwable.observe(viewLifecycleOwner, { error: String? -> errorObserver(error) })
        viewModel!!.errorMessage.observe(viewLifecycleOwner, { error: String? -> errorObserver(error) })
        binding!!.btnLogout.setOnClickListener { v: View? -> onLogoutClick() }
        if (savedInstanceState == null) {
            loadUserToken()
            viewModel!!.profileRequest()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun logoutObserver(status: Int) {
        deleteUserToken()
        navigateToLogin()
    }

    private fun profileObserver(profile: DataProfile?) {
        binding!!.textTesterName.text = profile!!.testerName
        binding!!.textLogin.text = profile.login
        binding!!.textEmail.text = profile.email
    }

    private fun errorObserver(error: String?) {
        binding!!.textErrorMessage.text = error
    }

    private fun onLogoutClick() {
        viewModel!!.logoutRequest()
    }

    private fun deleteUserToken() {
        val sp = requireActivity().getSharedPreferences(
                Const.SHARED_FILE, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString(Const.SHARED_TOKEN, "")
        editor.apply()
    }

    private fun navigateToLogin() {
        Navigation.findNavController(binding!!.root).popBackStack()
    }

    private fun loadUserToken() {
        val sp = requireActivity().getSharedPreferences(
                Const.SHARED_FILE, Context.MODE_PRIVATE)
        viewModel!!.setUserToken(sp.getString(Const.SHARED_TOKEN, ""))
    }
}