package com.hackathon.watertestinginstant.ui.customview

import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.widget.ProgressBar
import androidx.annotation.ColorRes
import android.opengl.ETC1.getHeight
import androidx.core.view.ViewCompat.setAlpha
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.R
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.google.android.material.circularreveal.cardview.CircularRevealCardView


@Suppress("DEPRECATION")
class ChangingStatusProgressBar(context: Context?) : ProgressBar(context) {

    fun changingProgressDrawable() {

    }

    fun changingProgressDrawableColor(@ColorRes colorRes: Int) {
        val progressDrawable = super.getProgressDrawable()
//        val color = Color.argb(100, 225F, 245F, 254F)
        progressDrawable.colorFilter = ColorFilter()
    }
}

fun ChangingStatusProgressBar.changeColor() {

}

class DrawableGradient internal constructor(colors: IntArray, cornerRadius: Int) :
    GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors) {
    init {

        try {
            this.shape = GradientDrawable.RECTANGLE
            this.gradientType = GradientDrawable.LINEAR_GRADIENT
            this.cornerRadius = cornerRadius.toFloat()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun SetTransparency(transparencyPercent: Int): DrawableGradient {
        this.alpha = 255 - 255 * transparencyPercent / 100

        return this
    }
}

fun View.changeBkgAnimate(colorFrom: Int, colorTo: Int){
    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
    colorAnimation.duration = 250 // milliseconds
    colorAnimation.addUpdateListener { animator -> this.setBackgroundColor(animator.animatedValue as Int) }
    colorAnimation.start()
}

fun CircularRevealCardView.circularCardViewChange(colorFrom: Int, colorTo: Int){
    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
    colorAnimation.duration = 250 // milliseconds
    colorAnimation.addUpdateListener { animator -> this.setCardBackgroundColor(animator.animatedValue as Int) }
    colorAnimation.start()
}