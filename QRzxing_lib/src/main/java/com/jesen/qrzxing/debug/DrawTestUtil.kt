package com.jesen.qrzxing.debug

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AnimDemoPendulumTest() {
    AnimDemoPendulum(
        modifier = Modifier.size(500.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(color = Color.Green, shape = CircleShape)
        )
    }
}

@Composable
fun AnimDemoPendulum(
    modifier: Modifier = Modifier,
    swingDuration: Int = 1500,
    startX: Float = .2f,
    endX: Float = .8f,
    topY: Float = .2f,
    bottomY: Float = .4f,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()

    BoxWithConstraints(
        modifier = modifier
    ) {

        val start = maxWidth * startX
        val end = maxWidth * endX
        val top = maxHeight * topY
        val bottom = maxHeight * bottomY

        val x by infiniteTransition.animateFloat(
            initialValue = start.value,
            targetValue = end.value,
            animationSpec = infiniteRepeatable(
                animation = tween(swingDuration, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        val y by infiniteTransition.animateFloat(
            initialValue = top.value,
            targetValue = bottom.value,
            animationSpec = infiniteRepeatable(
                animation = tween(swingDuration / 2, easing = LinearOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        Box(
            modifier = Modifier.offset(x = x.dp, y = y.dp)
        ) {
            content()
        }
    }
}