package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.core.view.ViewCompat
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentVotingBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.base.ContentFragment
import com.emikhalets.voteapp.viewmodel.VotingViewModel

class VotingFragment : ContentFragment<FragmentVotingBinding>(FragmentVotingBinding::inflate) {

    private lateinit var viewModel: VotingViewModel

    private var isFirstSelected = false
    private var isSecondSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
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
        outState.putBoolean(FIRST_SELECTED, isFirstSelected)
        outState.putBoolean(SECOND_SELECTED, isSecondSelected)
    }

    private fun initViews(savedInstanceState: Bundle) {
        isFirstSelected = savedInstanceState.getBoolean(FIRST_SELECTED)
        isSecondSelected = savedInstanceState.getBoolean(SECOND_SELECTED)
        binding.apply {
            if (isFirstSelected) imageVote1.setBackgroundResource(R.drawable.background_selected_image)
            else imageVote1.setBackgroundResource(R.drawable.background_unselected_image)
            if (isSecondSelected) imageVote2.setBackgroundResource(R.drawable.background_selected_image)
            else imageVote2.setBackgroundResource(R.drawable.background_unselected_image)
        }
    }

    private fun onViewLoaded() {
        setViewState(ViewState.LOADING)
        viewModel.setSelectedImage(null)
        viewModel.sendPrepareVotingRequest()
    }

    private fun initListeners() {
        viewModel.apply {
            image1.observe(viewLifecycleOwner) {
                ViewCompat.setTransitionName(binding.imageVote1, it.url)
                onImageLoaded(it.url, ImageNumber.FIRST)
            }

            image2.observe(viewLifecycleOwner) {
                ViewCompat.setTransitionName(binding.imageVote2, it.url)
                onImageLoaded(it.url, ImageNumber.SECOND)
            }

            error.observe(viewLifecycleOwner, EventObserver { toast(it) })

            prepareState.observe(viewLifecycleOwner, {
                setViewState(ViewState.LOADED)
                binding.btnVote.isEnabled = true
            })

            voteState.observe(viewLifecycleOwner, {
                binding.btnVote.isEnabled = true
                isFirstSelected = false
                isSecondSelected = false
                binding.imageVote1.setBackgroundResource(0)
                binding.imageVote2.setBackgroundResource(0)
            })
        }

        binding.apply {
            imageVote1.setOnClickListener { onImageClick(ImageNumber.FIRST) }
            imageVote2.setOnClickListener { onImageClick(ImageNumber.SECOND) }
            imageVote1.setOnLongClickListener(onImageLongClick(ImageNumber.FIRST))
            imageVote2.setOnLongClickListener(onImageLongClick(ImageNumber.SECOND))
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
        binding.btnVote.isEnabled = true
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

    private fun onImageLongClick(imageNumber: ImageNumber) = View.OnLongClickListener {
        when (imageNumber) {
            ImageNumber.FIRST -> {
                val url = viewModel.image1.value?.url.toString()
                if (url.isNotEmpty()) {
                    navigate(VotingFragmentDirections.actionVotingToImage(url),
                            binding.imageVote1 to url)
                }
            }
            ImageNumber.SECOND -> {
                val url = viewModel.image2.value?.url.toString()
                if (url.isNotEmpty()) {
                    navigate(VotingFragmentDirections.actionVotingToImage(url),
                            binding.imageVote2 to url)
                }
            }
        }
        true
    }

    private fun onVoteClick() {
        binding.btnVote.isEnabled = false
        viewModel.sendVoteRequest()
    }

    private fun setViewState(state: ViewState) {
        when (state) {
            ViewState.LOADING -> binding.progressBar.animShow()
            ViewState.LOADED -> binding.progressBar.animHide()
        }
    }

    private companion object {
        const val FIRST_SELECTED = "first_image_selected"
        const val SECOND_SELECTED = "selected_image_selected"
    }
}