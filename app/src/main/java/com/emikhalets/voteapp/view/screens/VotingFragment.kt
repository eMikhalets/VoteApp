package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentVotingBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.base.ContentFragment
import com.emikhalets.voteapp.viewmodel.VotingViewModel

class VotingFragment : ContentFragment<FragmentVotingBinding>(FragmentVotingBinding::inflate) {

    private lateinit var viewModel: VotingViewModel

    private var isVoteEnabled = false
    private var isFirstSelected = false
    private var isSecondSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity().title = getString(R.string.voting_title)
        if (savedInstanceState == null) onViewLoaded()
        else initViews(savedInstanceState)
        binding.btnVote.isEnabled = false
        initListeners()
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
        binding.apply {
            btnVote.isEnabled = isVoteEnabled
            if (isFirstSelected) imageVote1.setBackgroundResource(R.drawable.background_selected_image)
            else imageVote1.setBackgroundResource(R.drawable.background_unselected_image)
            if (isSecondSelected) imageVote2.setBackgroundResource(R.drawable.background_selected_image)
            else imageVote2.setBackgroundResource(R.drawable.background_unselected_image)
        }
    }

    private fun onViewLoaded() {
        setViewState(ViewState.LOADING)
        viewModel.sendPrepareVotingRequest {
            setViewState(ViewState.LOADED)
            isVoteEnabled = true
            binding.btnVote.isEnabled = true
        }
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
                binding.imageVote2.setBackgroundResource(R.drawable.background_unselected_image)
                isFirstSelected = true
                isSecondSelected = false
            }
            ImageNumber.SECOND -> {
                binding.imageVote1.setBackgroundResource(R.drawable.background_unselected_image)
                binding.imageVote2.setBackgroundResource(R.drawable.background_selected_image)
                isFirstSelected = false
                isSecondSelected = true
            }
        }
    }

    private fun onVoteClick() {
        isVoteEnabled = false
        binding.btnVote.isEnabled = false
        viewModel.sendVoteRequest { isSuccess, error ->
            isVoteEnabled = true
            binding.btnVote.isEnabled = true
            if (isSuccess) {
                isFirstSelected = false
                isSecondSelected = false
                binding.imageVote1.setBackgroundResource(0)
                binding.imageVote2.setBackgroundResource(0)
            } else {
                toastLong(error)
            }
        }
    }

    private fun setViewState(state: ViewState) {
        when (state) {
            ViewState.LOADING -> binding.progressBar.animShow()
            ViewState.LOADED -> binding.progressBar.animHide()
        }
    }

    private companion object {
        const val VOTE_ENABLE = "button_vote_enable"
        const val FIRST_SELECTED = "first_image_selected"
        const val SECOND_SELECTED = "selected_image_selected"
    }
}