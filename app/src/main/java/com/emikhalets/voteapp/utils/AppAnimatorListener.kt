package com.emikhalets.voteapp.utils

import android.animation.Animator

class AppAnimatorListener(
        private val start: () -> Unit = {},
        private val end: () -> Unit = {}
) : Animator.AnimatorListener {

    override fun onAnimationStart(animation: Animator?) {
        start()
    }

    override fun onAnimationEnd(animation: Animator?) {
        end()
    }

    override fun onAnimationCancel(animation: Animator?) {
    }

    override fun onAnimationRepeat(animation: Animator?) {
    }
}