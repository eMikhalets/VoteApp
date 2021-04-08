package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
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
        viewModel = injectViewModel(activity().viewModelFactory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity().title = getString(R.string.home_title)
        activity().drawer.enableDrawer()
        initRecyclerView()
        initListeners()
        if (savedInstanceState == null) onViewLoaded()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeUserListener()
    }

    private fun initRecyclerView() {
        imagesAdapter = ImagesAdapter(true, { url, view -> onImageClick(url, view) })
        binding.apply {
            listImages.setHasFixedSize(true)
            listImages.adapter = imagesAdapter
        }
    }

    private fun initListeners() {
        viewModel.apply {
            user.observe(viewLifecycleOwner) {
                activity().drawer.updateHeader(it)
            }
            images.observe(viewLifecycleOwner) {
                imagesAdapter.submitList(it)
                binding.apply {
                    layoutRefresh.isRefreshing = false
                    progress.visibility = android.view.View.GONE
                    listImages.visibility = android.view.View.VISIBLE
                    listImages.scrollToTop(this@HomeFragment)
                }
            }
        }
        binding.root.setOnRefreshListener { onRefreshInvoke() }
    }

    private fun onViewLoaded() {
        viewModel.apply {
            sendLatestImagesRequest()
            sendLoadUserDataRequest()
        }
    }

    private fun onRefreshInvoke() {
        viewModel.sendLatestImagesRequest(true)
    }

    private fun onImageClick(url: String, view: View) {
        val args = bundleOf(ARGS_PHOTO to url)
        navigate(R.id.action_home_to_image, args)
    }
}