package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import com.emikhalets.voteapp.databinding.FragmentImageBinding
import com.emikhalets.voteapp.utils.ARGS_PHOTO
import com.emikhalets.voteapp.utils.loadImage
import com.emikhalets.voteapp.view.base.ContentFragment

class ImageFragment : ContentFragment<FragmentImageBinding>(FragmentImageBinding::inflate) {

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