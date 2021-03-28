package com.emikhalets.voteapp.view.topimages

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentHomeBinding
import com.emikhalets.voteapp.test.createMockImages
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.view.adapters.ImagesAdapter
import com.emikhalets.voteapp.view.base.SecondaryFragment
import timber.log.Timber

class TopImagesFragment : SecondaryFragment(R.layout.fragment_top_images) {

    private val binding: FragmentHomeBinding by viewBinding()
    private lateinit var imagesAdapter: ImagesAdapter
    private lateinit var llm: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ACTIVITY.title = getString(R.string.top_images_title)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        imagesAdapter = ImagesAdapter(true)
        Timber.d(imagesAdapter.toString())
        llm = LinearLayoutManager(this.context)
        binding.listImages.layoutManager = llm
        binding.listImages.setHasFixedSize(true)
        binding.listImages.isNestedScrollingEnabled = false
        binding.listImages.adapter = imagesAdapter
        imagesAdapter.updateList(createMockImages().sortedByDescending { it.rating })
    }
}