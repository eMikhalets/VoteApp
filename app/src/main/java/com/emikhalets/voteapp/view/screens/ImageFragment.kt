package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.navArgs
import com.emikhalets.voteapp.databinding.FragmentImageBinding
import com.emikhalets.voteapp.utils.loadImage
import com.emikhalets.voteapp.view.base.ContentFragment

class ImageFragment : ContentFragment<FragmentImageBinding>(FragmentImageBinding::inflate) {

    private val args: ImageFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTransitionName(binding.root, args.url)
        binding.image.loadImage(args.url)
    }
}