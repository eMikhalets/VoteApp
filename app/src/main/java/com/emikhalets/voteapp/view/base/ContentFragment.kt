package com.emikhalets.voteapp.view.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.emikhalets.voteapp.utils.Inflate
import com.emikhalets.voteapp.utils.activity

abstract class ContentFragment<T : ViewBinding>(inflate: Inflate<T>) : BaseFragment<T>(inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        activity().supportActionBar?.show()
        activity().drawer.disableDrawer()
    }
}