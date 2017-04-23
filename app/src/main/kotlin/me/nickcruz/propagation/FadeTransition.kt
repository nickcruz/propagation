package me.nickcruz.propagation

import android.graphics.Rect
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.transition.CircularPropagation
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup

/**
 * Created by Nick Cruz on 4/23/17
 */
class FadeTransition(val sceneRoot: ViewGroup) {

    val targets: Collection<View>
    val transition: Transition

    init {
        targets = sceneRoot.getChildren()

        transition = Fade(Fade.IN)
        transition.interpolator = LinearOutSlowInInterpolator()
        transition.propagation = CircularPropagation()
        transition.duration = 800
        transition.epicenterCallback = sceneRoot.getChildAt(0).asEpicenter()
    }

    fun start() {
        targets.forEach { it.visibility = View.INVISIBLE }

        TransitionManager.beginDelayedTransition(sceneRoot, transition)

        targets.forEach { it.visibility = View.VISIBLE }
    }

    private fun ViewGroup.getChildren(): Collection<View> = (0 until childCount).map { getChildAt(it) }

    private fun View.asEpicenter() : Transition.EpicenterCallback {
        val viewRect = Rect()
        getGlobalVisibleRect(viewRect)
        return object : Transition.EpicenterCallback() {
            override fun onGetEpicenter(transition: Transition?): Rect = viewRect
        }
    }
}