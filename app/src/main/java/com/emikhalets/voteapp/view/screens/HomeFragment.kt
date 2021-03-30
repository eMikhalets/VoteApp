package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentHomeBinding
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.ARGS_PHOTO
import com.emikhalets.voteapp.utils.navigate
import com.emikhalets.voteapp.view.adapters.ImagesAdapter
import com.emikhalets.voteapp.viewmodel.HomeViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()
    private val viewModel: HomeViewModel by viewModels()

    private val imagesAdapter = ImagesAdapter(true) { url, v -> onImageClick(url, v) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ACTIVITY.drawer.enableDrawer()
        ACTIVITY.title = getString(R.string.home_title)
        initRecyclerView()
        initListeners()
        onViewLoaded()
    }

    private fun initRecyclerView() {
        binding.apply {
            listImages.setHasFixedSize(true)
            listImages.isNestedScrollingEnabled = false
            listImages.adapter = imagesAdapter
        }
    }

    private fun initListeners() {
        viewModel.images.observe(viewLifecycleOwner) { imagesAdapter.submitList(it) }
    }

    private fun onViewLoaded() {
        viewModel.sendLatestImagesRequest {
            imagesAdapter.submitList(it)
        }
    }

    private fun onImageClick(url: String, view: View) {
        val args = bundleOf(ARGS_PHOTO to url)
        navigate(R.id.action_home_to_image, args)
    }
}