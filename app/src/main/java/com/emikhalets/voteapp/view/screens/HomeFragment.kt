package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentHomeBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.adapters.ImagesAdapter
import com.emikhalets.voteapp.view.base.MainFragment
import com.emikhalets.voteapp.viewmodel.HomeViewModel

class HomeFragment : MainFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private lateinit var viewModel: HomeViewModel
    private lateinit var imagesAdapter: ImagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity().title = getString(R.string.home_title)
        if (savedInstanceState == null) onViewLoaded()
        initRecyclerView()
        initListeners()
    }

    private fun initRecyclerView() {
        imagesAdapter = ImagesAdapter(true, { url, view -> onImageClick(url, view) })
        binding.listImages.apply {
            setHasFixedSize(true)
            adapter = imagesAdapter
            handleTransition(this@HomeFragment)
        }
    }

    private fun initListeners() {
        viewModel.apply {
            user.observe(viewLifecycleOwner, {
                activity().drawer.updateHeader(it)
            })

            images.observe(viewLifecycleOwner, {
                setViewState(ViewState.LOADED)
                if (it.isEmpty()) toast(R.string.app_toast_no_images_on_server)
                else imagesAdapter.submitList(it)
            })

            error.observe(viewLifecycleOwner, EventObserver {
                setViewState(ViewState.LOADED)
                toast(it)
            })
        }

        binding.root.setOnRefreshListener { onRefreshInvoke() }
    }

    private fun onViewLoaded() {
        setViewState(ViewState.LOADING)
        viewModel.apply {
            viewModel.sendLatestImagesRequest()
            viewModel.sendLoadUserDataRequest()
        }
    }

    private fun onRefreshInvoke() {
        setViewState(ViewState.LOADING)
        viewModel.sendLatestImagesRequest(true)
    }

    private fun onImageClick(url: String, view: View) {
        navigate(HomeFragmentDirections.actionHomeToImage(url), view to url)
    }

    private fun setViewState(state: ViewState) {
        binding.apply {
            layoutRefresh.isRefreshing = false
            when (state) {
                ViewState.LOADING -> progressBar.animShow()
                ViewState.LOADED -> progressBar.animHide()
            }
        }
    }
}