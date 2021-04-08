package com.emikhalets.voteapp.view.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.emikhalets.voteapp.utils.Inflate
import com.emikhalets.voteapp.utils.activity
import com.emikhalets.voteapp.utils.hideKeyboard

abstract class AuthFragment<T : ViewBinding>(inflate: Inflate<T>) : BaseFragment<T>(inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        activity().supportActionBar?.hide()
        activity().drawer.hideDrawer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard()
    }
}