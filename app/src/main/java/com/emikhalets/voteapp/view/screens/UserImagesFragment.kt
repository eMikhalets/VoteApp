package com.emikhalets.voteapp.view.screens

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentUserImagesBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.TakeImageContract
import com.emikhalets.voteapp.view.adapters.ImagesAdapter
import com.emikhalets.voteapp.view.base.ContentFragment
import com.emikhalets.voteapp.viewmodel.UserImagesViewModel

class UserImagesFragment : ContentFragment<FragmentUserImagesBinding>(FragmentUserImagesBinding::inflate) {

    private lateinit var takeImageResult: ActivityResultLauncher<Int>
    private lateinit var imagesAdapter: ImagesAdapter
    private lateinit var viewModel: UserImagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
        takeImageResult = registerForActivityResult(TakeImageContract(activity())) {
            onTakeImageResult(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity().title = getString(R.string.images_title)
        setHasOptionsMenu(true)
        initRecyclerView()
        initListeners()
        if (savedInstanceState == null) onViewLoaded()
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
        imagesAdapter = ImagesAdapter(
                false,
                { url, v -> onImageClick(url, v) },
                { name -> onDeleteImageClick(name) }
        )
        binding.listImages.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            adapter = imagesAdapter
            handleTransition(this@UserImagesFragment)
        }
    }

    // Use notifyDataSetChanged because it gives an exception when deleting a image
    // Exception: IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter
    //            positionViewHolder (positions of holders not updated)
    private fun initListeners() {
        viewModel.images.observe(viewLifecycleOwner, {
            setViewState(ViewState.LOADED)
            if (imagesAdapter.currentList.isEmpty()) imagesAdapter.submitList(null)
            imagesAdapter.submitList(it)
            imagesAdapter.notifyDataSetChanged()
            binding.listImages.scrollToTop(this)
        })

        viewModel.error.observe(viewLifecycleOwner, EventObserver {
            setViewState(ViewState.LOADED)
            toast(R.string.app_toast_no_images_on_server)
        })
    }

    private fun onViewLoaded() {
        setViewState(ViewState.LOADING)
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
        takeImageResult.launch(1000)
        return true
    }

    private fun onTakeImageResult(uri: Uri?) {
        setViewState(ViewState.LOADING)
        viewModel.sendSaveImageRequest(uri.toString())
    }

    private fun onImageClick(url: String, view: View) {
        navigate(UserImagesFragmentDirections.actionUserImagesToImage(url), view to url)
    }

    private fun onDeleteImageClick(name: String): Boolean {
        navigate(UserImagesFragmentDirections.actionUserImagesToDeleteImage(name))
        return true
    }

    private fun setViewState(state: ViewState) {
        when (state) {
            ViewState.LOADING -> binding.progressBar.animShow()
            ViewState.LOADED -> binding.progressBar.animHide()
        }
    }
}