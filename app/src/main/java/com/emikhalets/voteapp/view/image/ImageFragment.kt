package com.emikhalets.voteapp.view.image

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentImageBinding
import com.emikhalets.voteapp.utils.ARGS_PHOTO
import com.emikhalets.voteapp.view.base.SecondaryFragment

class ImageFragment : SecondaryFragment(R.layout.fragment_image) {

    private val binding: FragmentImageBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewLoaded()
    }

    private fun onViewLoaded() {
        sharedElementEnterTransition =
                TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition =
                TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        binding.image.load(arguments?.getString(ARGS_PHOTO))
    }
}