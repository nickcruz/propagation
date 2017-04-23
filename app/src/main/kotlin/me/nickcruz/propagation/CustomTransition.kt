package me.nickcruz.propagation

import android.graphics.Rect
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.transition.*
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator

/**
 * Created by Nick Cruz on 4/23/17
 */
class CustomTransition(
        val sceneRoot: ViewGroup,
        var startingView: View? = null,
        val transition: Transition = Fade(Fade.IN),
        duration: Long = 800,
        interpolator: Interpolator = LinearOutSlowInInterpolator(),
        propagation: TransitionPropagation = CircularPropagation()) {

    val targets: Collection<View>

    init {
        targets = (0 until sceneRoot.childCount).map { sceneRoot.getChildAt(it) }
        transition.interpolator = interpolator
        transition.propagation = propagation
        transition.duration = duration
    }

    fun start() {
        if (startingView == null && sceneRoot.childCount > 0) {
            startingView = sceneRoot.getChildAt(0)
        }
        transition.epicenterCallback = (startingView ?: sceneRoot).asEpicenter()

        targets.forEach { it.visibility = View.INVISIBLE }

        TransitionManager.beginDelayedTransition(sceneRoot, transition)

        targets.forEach { it.visibility = View.VISIBLE }
    }

    private fun View.asEpicenter() : Transition.EpicenterCallback {
        val viewRect = Rect()
        getGlobalVisibleRect(viewRect)
        return object : Transition.EpicenterCallback() {
            override fun onGetEpicenter(transition: Transition?): Rect = viewRect
        }
    }
}