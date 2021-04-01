package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentVotingBinding
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.ImageNumber
import com.emikhalets.voteapp.utils.loadImage
import com.emikhalets.voteapp.view.base.WithDrawerFragment
import com.emikhalets.voteapp.viewmodel.VotingViewModel

class VotingFragment : WithDrawerFragment(R.layout.fragment_voting) {

    private val binding: FragmentVotingBinding by viewBinding()
    private val viewModel: VotingViewModel by viewModels()

    private var isVoteEnabled = false
    private var isFirstSelected = false
    private var isSecondSelected = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ACTIVITY.title = getString(R.string.voting_title)
        if (savedInstanceState != null) initViews(savedInstanceState)
        initListeners()
        onViewLoaded()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(VOTE_ENABLE, isVoteEnabled)
        outState.putBoolean(FIRST_SELECTED, isFirstSelected)
        outState.putBoolean(SECOND_SELECTED, isSecondSelected)
    }

    private fun initViews(savedInstanceState: Bundle) {
        isVoteEnabled = savedInstanceState.getBoolean(VOTE_ENABLE)
        isFirstSelected = savedInstanceState.getBoolean(FIRST_SELECTED)
        isSecondSelected = savedInstanceState.getBoolean(SECOND_SELECTED)
        binding.btnVote.isEnabled = isVoteEnabled
        if (isFirstSelected) binding.imageVote1.setBackgroundResource(R.drawable.background_selected_image)
        if (isSecondSelected) binding.imageVote2.setBackgroundResource(R.drawable.background_selected_image)
    }

    private fun initListeners() {
        viewModel.apply {
            image1.observe(viewLifecycleOwner) { onImageLoaded(it.url, ImageNumber.FIRST) }
            image2.observe(viewLifecycleOwner) { onImageLoaded(it.url, ImageNumber.SECOND) }
        }
        binding.apply {
            imageVote1.setOnClickListener { onImageClick(ImageNumber.FIRST) }
            imageVote2.setOnClickListener { onImageClick(ImageNumber.SECOND) }
            btnVote.setOnClickListener { onVoteClick() }
        }
    }

    private fun onViewLoaded() {
        viewModel.sendPrepareVotingRequest {
            isVoteEnabled = true
            binding.btnVote.isEnabled = true
        }
    }

    private fun onImageLoaded(url: String, image: ImageNumber) {
        when (image) {
            ImageNumber.FIRST -> binding.imageVote1.loadImage(url)
            ImageNumber.SECOND -> binding.imageVote2.loadImage(url)
        }
    }

    private fun onImageClick(image: ImageNumber) {
        viewModel.setSelectedImage(image)
        when (image) {
            ImageNumber.FIRST -> {
                binding.imageVote1.setBackgroundResource(R.drawable.background_selected_image)
                binding.imageVote2.setBackgroundResource(0)
                isFirstSelected = true
                isSecondSelected = false
            }
            ImageNumber.SECOND -> {
                binding.imageVote2.setBackgroundResource(R.drawable.background_selected_image)
                binding.imageVote1.setBackgroundResource(0)
                isFirstSelected = false
                isSecondSelected = true
            }
        }
    }

    private fun onVoteClick() {
        isVoteEnabled = false
        binding.btnVote.isEnabled = false
        viewModel.sendVoteRequest {
            isVoteEnabled = true
            binding.btnVote.isEnabled = true
            isFirstSelected = false
            isSecondSelected = false
            binding.imageVote1.setBackgroundResource(0)
            binding.imageVote2.setBackgroundResource(0)
        }
    }

    private companion object {
        const val VOTE_ENABLE = "button_vote_enable"
        const val FIRST_SELECTED = "first_image_selected"
        const val SECOND_SELECTED = "selected_image_selected"
    }
}