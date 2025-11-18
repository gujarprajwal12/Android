package com.psg.android.presentaion.flippage.ui
import android.view.View
import androidx.viewpager2.widget.ViewPager2

class FlipPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.cameraDistance = 20000f

        when {
            position <= -1f || position >= 1f -> {
                page.alpha = 0f
            }
            position == 0f -> {
                page.alpha = 1f
                page.rotationY = 0f
            }
            else -> {
                page.alpha = 1f
                val rotation = position * -180f
                page.rotationY = rotation

                val normalized = Math.abs(position)
                page.alpha = 1f - normalized * 0.25f
            }
        }
    }
}
