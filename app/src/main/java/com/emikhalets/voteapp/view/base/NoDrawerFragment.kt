package com.emikhalets.voteapp.view.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.hideKeyboard

abstract class NoDrawerFragment(layout: Int) : Fragment(layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ACTIVITY.drawer.hideDrawer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard()
    }
}