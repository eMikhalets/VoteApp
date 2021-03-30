package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentVotingBinding
import com.emikhalets.voteapp.view.base.WithDrawerFragment

class VotingFragment : WithDrawerFragment(R.layout.fragment_voting) {

    private val binding: FragmentVotingBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}