package com.hackathon.watertestinginstant.ui.customview

import android.view.View
import android.view.animation.AnimationUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hackathon.watertestinginstant.R

fun View.fadeOut() {
    val animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
    animation.start()
    this.startAnimation(animation)
}