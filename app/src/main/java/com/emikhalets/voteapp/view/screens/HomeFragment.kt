package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentHomeBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.adapters.ImagesAdapter
import com.emikhalets.voteapp.viewmodel.HomeViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()
    private val imagesAdapter = ImagesAdapter(true, { url, v -> onImageClick(url, v) })
    lateinit var viewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = injectViewModel(ACTIVITY.viewModelFactory)
        ACTIVITY.drawer.enableDrawer()
        ACTIVITY.title = getString(R.string.home_title)
        initRecyclerView()
        initListeners()
        onViewLoaded()
    }

    private fun initRecyclerView() {
        binding.apply {
            listImages.setHasFixedSize(true)
            listImages.adapter = imagesAdapter
        }
    }

    private fun initListeners() {
        viewModel.images.observe(viewLifecycleOwner) {
            imagesAdapter.submitList(it)
            binding.apply {
                root.isRefreshing = false
                progress.visibility = View.GONE
                listImages.visibility = View.VISIBLE
                listImages.scrollToTop()
            }
        }
        binding.root.setOnRefreshListener { onRefreshInvoke() }
    }

    private fun onViewLoaded() {
        viewModel.sendLatestImagesRequest()
    }

    private fun onRefreshInvoke() {
        viewModel.sendLatestImagesRequest(true)
    }

    private fun onImageClick(url: String, view: View) {
        val args = bundleOf(ARGS_PHOTO to url)
        navigateOld(R.id.action_home_to_image, args)
    }
}