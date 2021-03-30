package com.emikhalets.voteapp.view.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.emikhalets.voteapp.utils.ACTIVITY

abstract class WithDrawerFragment(layout: Int) : Fragment(layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ACTIVITY.drawer.disableDrawer()
    }
}