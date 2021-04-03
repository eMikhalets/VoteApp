package com.emikhalets.voteapp.view.screens

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentUserImagesBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.TakeImageContract
import com.emikhalets.voteapp.view.adapters.ImagesAdapter
import com.emikhalets.voteapp.view.base.WithDrawerFragment
import com.emikhalets.voteapp.viewmodel.UserImagesViewModel

class UserImagesFragment : WithDrawerFragment(R.layout.fragment_user_images) {

    private val binding: FragmentUserImagesBinding by viewBinding()
    lateinit var viewModel: UserImagesViewModel

    private val imagesAdapter = ImagesAdapter(
            false,
            { url, v -> onImageClick(url, v) },
            { name, pos -> onDeleteImageClick(name, pos) }
    )

    private val takeImageResult = registerForActivityResult(TakeImageContract()) {
        onTakeImageResult(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = injectViewModel(ACTIVITY.viewModelFactory)
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
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            adapter = imagesAdapter
        }
    }

    // Use notifyDataSetChanged because it gives an exception when deleting a image
    // Exception: IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter
    //            positionViewHolder (positions of holders not updated)
    private fun initListeners() {
        viewModel.images.observe(viewLifecycleOwner) {
            imagesAdapter.submitList(it)
            imagesAdapter.notifyDataSetChanged()
            binding.listImages.scrollToTop()
        }
    }

    private fun onViewLoaded() {
        viewModel.sendLoadUserImagesRequest()
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
//            binding.listImages.scrollToTop()
        }
    }

    private fun onImageClick(url: String, view: View) {
        val args = bundleOf(ARGS_PHOTO to url)
        navigate(R.id.action_userImages_to_image, args)
    }

    private fun onDeleteImageClick(name: String, pos: Int): Boolean {
        val args = bundleOf(ARGS_NAME to name, ARGS_POS to pos)
        navigate(R.id.action_userImages_to_deleteImage, args)
        return true
    }
}