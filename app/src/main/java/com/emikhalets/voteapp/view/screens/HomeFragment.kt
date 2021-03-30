package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentHomeBinding
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.view.adapters.ImagesAdapter

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()
    private lateinit var imagesAdapter: ImagesAdapter
    private lateinit var llm: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ACTIVITY.drawer.enableDrawer()
        ACTIVITY.title = getString(R.string.home_title)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        imagesAdapter = ImagesAdapter(true) { _, _ ->}
        llm = LinearLayoutManager(this.context)
        binding.apply {
            listImages.layoutManager = llm
            listImages.setHasFixedSize(true)
            listImages.isNestedScrollingEnabled = false
            listImages.adapter = imagesAdapter
        }
//        imagesAdapter.updateList(createMockImages())
    }
}