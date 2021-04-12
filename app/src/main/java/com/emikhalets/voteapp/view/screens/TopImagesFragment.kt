package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
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
        if (savedInstanceState == null) onViewLoaded()
        initRecyclerView()
        initListeners()
    }

    private fun initRecyclerView() {
        imagesAdapter = ImagesAdapter(true, { url, v -> onImageClick(url, v) })
        binding.listImages.apply {
            setHasFixedSize(true)
            adapter = imagesAdapter
            handleTransition(this@TopImagesFragment)
        }
    }

    private fun initListeners() {
        viewModel.images.observe(viewLifecycleOwner) {
            setViewState(ViewState.LOADED)
            imagesAdapter.submitList(it)
        }

        viewModel.error.observe(viewLifecycleOwner, EventObserver {
            setViewState(ViewState.LOADED)
            toast(R.string.app_toast_no_images_on_server)
        })
    }

    private fun onViewLoaded() {
        setViewState(ViewState.LOADING)
        viewModel.sendLoadTopImagesRequest()
    }

    private fun onImageClick(url: String, view: View) {
        navigate(TopImagesFragmentDirections.actionTopImagesToImage(url), view to url)
    }

    private fun setViewState(state: ViewState) {
        when (state) {
            ViewState.LOADING -> binding.progressBar.animShow()
            ViewState.LOADED -> binding.progressBar.animHide()
        }
    }
}