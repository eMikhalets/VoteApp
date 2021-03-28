package com.emikhalets.voteapp.view.user_images

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentImagesBinding
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.test.createMockImages
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.toast
import com.emikhalets.voteapp.view.adapters.ImagesAdapter
import com.emikhalets.voteapp.view.base.SecondaryFragment

class UserImagesFragment : SecondaryFragment(R.layout.fragment_images) {

    private val binding: FragmentImagesBinding by viewBinding()
    private lateinit var imagesAdapter: ImagesAdapter
    private lateinit var llm: LinearLayoutManager
    private val imagesList = mutableListOf<Image>()
    private var isDescending = true

    private val takeImageResult = registerForActivityResult(TakeImageContract()) {
        it?.let { uri -> toast(uri.toString()) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        ACTIVITY.title = getString(R.string.images_title)
        initRecyclerView()
        initListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_images, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_action_sort_date -> sortByDate()
            R.id.menu_action_sort_rating -> sortByRating()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun sortByDate(): Boolean {
        if (isDescending) imagesList.sortByDescending { it.date }
        else imagesList.sortBy { it.date }
        isDescending = !isDescending
        imagesAdapter.updateList(imagesList)
        return true
    }

    private fun sortByRating(): Boolean {
        if (isDescending) imagesList.sortByDescending { it.rating }
        else imagesList.sortBy { it.date }
        isDescending = !isDescending
        imagesAdapter.updateList(imagesList)
        return true
    }

    private fun initRecyclerView() {
        imagesAdapter = ImagesAdapter()
        llm = LinearLayoutManager(this.context)
        binding.apply {
            listImages.layoutManager = llm
            listImages.setHasFixedSize(true)
            listImages.isNestedScrollingEnabled = false
            listImages.adapter = imagesAdapter
        }
        imagesList.addAll(createMockImages())
        imagesAdapter.updateList(imagesList)
    }

    private fun initListeners() {
        binding.btnTakeImage.setOnClickListener { onTakeImageClick() }
    }

    private fun onTakeImageClick() {
        takeImageResult.launch(250)
    }
}