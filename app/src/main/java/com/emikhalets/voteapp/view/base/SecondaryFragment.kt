package com.emikhalets.voteapp.view.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentHomeBinding
import com.emikhalets.voteapp.utils.ACTIVITY

abstract class SecondaryFragment(layout: Int) : Fragment(layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ACTIVITY.drawer.disableDrawer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ACTIVITY.drawer.enableDrawer()
    }
}