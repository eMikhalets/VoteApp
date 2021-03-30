package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentTopImagesBinding
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.view.adapters.ImagesAdapter
import com.emikhalets.voteapp.view.base.WithDrawerFragment

class TopImagesFragment : WithDrawerFragment(R.layout.fragment_top_images) {

    private val binding: FragmentTopImagesBinding by viewBinding()
    private lateinit var imagesAdapter: ImagesAdapter
    private lateinit var llm: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ACTIVITY.title = getString(R.string.top_images_title)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        imagesAdapter = ImagesAdapter(true) {}
        llm = LinearLayoutManager(this.context)
        binding.apply {
            listImages.layoutManager = llm
            listImages.setHasFixedSize(true)
            listImages.isNestedScrollingEnabled = false
            listImages.adapter = imagesAdapter
        }
//        imagesAdapter.updateList(createMockImages().sortedByDescending { it.rating })
    }
}