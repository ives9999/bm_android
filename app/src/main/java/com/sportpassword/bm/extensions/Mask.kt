package com.sportpassword.bm.extensions

import android.animation.Animator
import android.view.View

object Mask {
    val alpha = 0.8f
    val duration: Long = 100
    fun show(view: View) {
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate().setDuration(duration).alpha(alpha).setListener(object: Animator.AnimatorListener {
            override fun onAnimationEnd(p0: Animator) {
                view.visibility = View.VISIBLE
            }
            override fun onAnimationRepeat(p0: Animator) {}
            override fun onAnimationCancel(p0: Animator) {}
            override fun onAnimationStart(p0: Animator) {}
        })
    }
    fun hide(mask: View) {
        mask.visibility = View.VISIBLE
        mask.animate().setDuration(duration).alpha(0f).setListener(object: Animator.AnimatorListener {
            override fun onAnimationEnd(p0: Animator) {
                mask.visibility = View.INVISIBLE
            }
            override fun onAnimationRepeat(p0: Animator) {}
            override fun onAnimationCancel(p0: Animator) {}
            override fun onAnimationStart(p0: Animator) {}
        })
    }
}