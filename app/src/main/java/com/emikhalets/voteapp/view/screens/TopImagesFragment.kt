package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentTopImagesBinding
import com.emikhalets.voteapp.utils.ARGS_PHOTO
import com.emikhalets.voteapp.utils.activity
import com.emikhalets.voteapp.utils.injectViewModel
import com.emikhalets.voteapp.utils.navigate
import com.emikhalets.voteapp.view.adapters.ImagesAdapter
import com.emikhalets.voteapp.view.base.WithDrawerFragment
import com.emikhalets.voteapp.viewmodel.TopImagesViewModel

class TopImagesFragment : WithDrawerFragment(R.layout.fragment_top_images) {

    private val binding: FragmentTopImagesBinding by viewBinding()
    private lateinit var imagesAdapter: ImagesAdapter
    lateinit var viewModel: TopImagesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = injectViewModel(activity().viewModelFactory)
        activity().title = getString(R.string.top_images_title)
        initRecyclerView()
        initListeners()
        if (savedInstanceState == null) onViewLoaded()
    }

    private fun initRecyclerView() {
        imagesAdapter = ImagesAdapter(true, { url, v -> onImageClick(url, v) })
        binding.apply {
            listImages.setHasFixedSize(true)
            listImages.isNestedScrollingEnabled = false
            listImages.adapter = imagesAdapter
        }
    }

    private fun initListeners() {
        viewModel.images.observe(viewLifecycleOwner) {
            imagesAdapter.submitList(it)
        }
    }

    private fun onViewLoaded() {
        viewModel.sendLoadTopImagesRequest()
    }

    private fun onImageClick(url: String, view: View) {
        val args = bundleOf(ARGS_PHOTO to url)
        navigate(R.id.action_topImages_to_image, args)
    }
}