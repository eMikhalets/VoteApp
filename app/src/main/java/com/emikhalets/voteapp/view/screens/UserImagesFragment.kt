package com.emikhalets.voteapp.view.screens

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentUserImagesBinding
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.view.TakeImageContract
import com.emikhalets.voteapp.view.adapters.ImagesAdapter
import com.emikhalets.voteapp.view.base.WithDrawerFragment
import com.emikhalets.voteapp.viewmodel.UserImagesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserImagesFragment : WithDrawerFragment(R.layout.fragment_user_images) {

    private val binding: FragmentUserImagesBinding by viewBinding()
    private val viewModel: UserImagesViewModel by viewModels()

    private val imagesAdapter = ImagesAdapter(false) { onImageClick(it) }

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
            R.id.menu_action_sort_date -> onSortByDateClick()
            R.id.menu_action_sort_rating -> onSortByRatingClick()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            listImages.setHasFixedSize(true)
            listImages.isNestedScrollingEnabled = false
            listImages.adapter = imagesAdapter
        }
    }

    private fun initListeners() {
        binding.btnTakeImage.setOnClickListener { onTakeImageClick() }
    }

    private fun onViewLoaded() {
        viewModel.sendLoadUserImagesRequest {
            imagesAdapter.submitList(it)
        }
    }

    private fun onSortByRatingClick(): Boolean {
        viewModel.sortImagesByRating {
            imagesAdapter.submitList(it)
            lifecycleScope.launch {
                delay(100)
                binding.listImages.smoothScrollToPosition(0)
            }
        }
        return true
    }

    private fun onSortByDateClick(): Boolean {
        viewModel.sortImagesByDate {
            imagesAdapter.submitList(it)
            lifecycleScope.launch {
                delay(100)
                binding.listImages.smoothScrollToPosition(0)
            }
        }
        return true
    }

    private fun onTakeImageClick() {
        takeImageResult.launch(500)
    }

    private fun onTakeImageResult(uri: Uri?) {
        viewModel.sendSaveImageRequest(uri) { images ->
            imagesAdapter.submitList(images)
            lifecycleScope.launch {
                delay(100)
                binding.listImages.smoothScrollToPosition(0)
            }
        }
    }

    private fun onImageClick(url: String) {
    }
}