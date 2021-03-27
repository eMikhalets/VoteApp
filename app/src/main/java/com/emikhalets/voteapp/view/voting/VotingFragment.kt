package com.emikhalets.voteapp.view.voting

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.emikhalets.voteapp.databinding.FragmentVotingBinding
import com.emikhalets.voteapp.utils.Const

class VotingFragment : Fragment() {
    private var viewModel: VotingViewModel? = null
    private var binding: FragmentVotingBinding? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentVotingBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(VotingViewModel::class.java)
        viewModel!!.errorMessage.observe(viewLifecycleOwner, { error: String? -> errorObserver(error) })
        viewModel!!.throwable.observe(viewLifecycleOwner, { error: String? -> errorObserver(error) })
        viewModel!!.voting.observe(viewLifecycleOwner, { status: Int -> votingObserver(status) })
        binding!!.imageVoting0.setOnClickListener { v: View? -> onClickFirstImage() }
        binding!!.imageVoting1.setOnClickListener { v: View? -> onClickSecondImage() }
        if (savedInstanceState == null) {
            loadUserToken()
            viewModel!!.voteCreateRequest()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun errorObserver(error: String?) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    private fun votingObserver(status: Int) {
        viewModel!!.voteCreateRequest()
    }

    private fun onClickFirstImage() {
        viewModel!!.voteRequest(0)
    }

    private fun onClickSecondImage() {
        viewModel!!.voteRequest(1)
    }

    private fun loadUserToken() {
        val sp = requireActivity().getSharedPreferences(
                Const.SHARED_FILE, Context.MODE_PRIVATE)
        viewModel!!.setUserToken(sp.getString(Const.SHARED_TOKEN, ""))
    }
}