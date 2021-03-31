package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentImageBinding
import com.emikhalets.voteapp.utils.ARGS_PHOTO
import com.emikhalets.voteapp.utils.loadImage
import com.emikhalets.voteapp.view.base.WithDrawerFragment

class ImageFragment : WithDrawerFragment(R.layout.fragment_image) {

    private val binding: FragmentImageBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedElementEnterTransition =
                TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        arguments?.let {
            val url = it.getString(ARGS_PHOTO)
            binding.image.loadImage(url ?: "no image")
        }
    }
}