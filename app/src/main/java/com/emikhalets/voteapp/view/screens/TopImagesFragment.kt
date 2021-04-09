package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentTopImagesBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.adapters.ImagesAdapter
import com.emikhalets.voteapp.view.base.ContentFragment
import com.emikhalets.voteapp.viewmodel.TopImagesViewModel

class TopImagesFragment : ContentFragment<FragmentTopImagesBinding>(FragmentTopImagesBinding::inflate) {

    private lateinit var imagesAdapter: ImagesAdapter
    private lateinit var viewModel: TopImagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            setViewState(ViewState.LOADED)
            imagesAdapter.submitList(it)
        }
    }

    private fun onViewLoaded() {
        setViewState(ViewState.LOADING)
        viewModel.sendLoadTopImagesRequest()
    }

    private fun onImageClick(url: String, view: View) {
        val args = bundleOf(ARGS_PHOTO to url)
        navigate(R.id.action_topImages_to_image, args)
    }

    private fun setViewState(state: ViewState) {
        when (state) {
            ViewState.LOADING -> binding.progressBar.animShow()
            ViewState.LOADED -> binding.progressBar.animHide()
        }
    }
}