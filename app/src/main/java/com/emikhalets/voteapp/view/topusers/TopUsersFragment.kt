package com.emikhalets.voteapp.view.topusers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.emikhalets.voteapp.databinding.FragmentTopUsersBinding
import com.emikhalets.voteapp.utils.Const

class TopUsersFragment : Fragment() {
    private var viewModel: TopUsersViewModel? = null
    private var binding: FragmentTopUsersBinding? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentTopUsersBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(TopUsersViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun loadUserToken() {
        val sp = requireActivity().getSharedPreferences(
                Const.SHARED_FILE, Context.MODE_PRIVATE)
        viewModel!!.setUserToken(sp.getString(Const.SHARED_TOKEN, ""))
    }
}