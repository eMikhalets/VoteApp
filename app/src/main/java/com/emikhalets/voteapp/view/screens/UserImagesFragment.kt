package com.emikhalets.voteapp.view.screens

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentUserImagesBinding
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.ARGS_PHOTO
import com.emikhalets.voteapp.utils.navigate
import com.emikhalets.voteapp.view.TakeImageContract
import com.emikhalets.voteapp.view.adapters.ImagesAdapter
import com.emikhalets.voteapp.view.base.WithDrawerFragment
import com.emikhalets.voteapp.viewmodel.UserImagesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserImagesFragment : WithDrawerFragment(R.layout.fragment_user_images) {

    private val binding: FragmentUserImagesBinding by viewBinding()
    private val viewModel: UserImagesViewModel by viewModels()

    private val imagesAdapter = ImagesAdapter(false) { url, v -> onImageClick(url, v) }
    private val llm = LinearLayoutManager(context)

    private val takeImageResult = registerForActivityResult(TakeImageContract()) {
        onTakeImageResult(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        ACTIVITY.title = getString(R.string.images_title)
        initRecyclerView()
        initListeners()
        onViewLoaded()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_images, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_action_add_image -> onTakeImageClick()
            R.id.menu_action_sort_date -> onSortByDateClick()
            R.id.menu_action_sort_rating -> onSortByRatingClick()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initRecyclerView() {
        binding.listImages.apply {
            layoutManager = llm
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            adapter = imagesAdapter
        }
    }

    private fun initListeners() {
        viewModel.images.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                imagesAdapter.submitList(it)
                delay(100)
                binding.listImages.smoothScrollToPosition(0)
            }
        }
    }

    private fun onViewLoaded() {
        viewModel.sendLoadUserImagesRequest {
            imagesAdapter.submitList(it)
        }
    }

    private fun onSortByRatingClick(): Boolean {
        viewModel.sortImagesByRating()
        return true
    }

    private fun onSortByDateClick(): Boolean {
        viewModel.sortImagesByDate()
        return true
    }

    private fun onTakeImageClick(): Boolean {
        takeImageResult.launch(500)
        return true
    }

    private fun onTakeImageResult(uri: Uri?) {
        viewModel.sendSaveImageRequest(uri) { images ->
            if (imagesAdapter.currentList.isEmpty()) imagesAdapter.submitList(null)
            imagesAdapter.submitList(images)
            lifecycleScope.launch {
                delay(100)
                binding.listImages.smoothScrollToPosition(0)
            }
        }
    }

    private fun onImageClick(url: String, view: View) {
        val args = bundleOf(ARGS_PHOTO to url)
        navigate(R.id.action_userImages_to_image, args)
    }
}