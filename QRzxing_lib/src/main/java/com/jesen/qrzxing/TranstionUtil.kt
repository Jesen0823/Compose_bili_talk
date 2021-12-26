package com.jesen.qrzxing

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color

val goldenrod = Color(0xFFDAA520)
val primaryColor = Color(0xffff9db5)
val primaryDeepColor = Color(0xfffb7299)

@Composable
fun floatTransition(
    initialValue: Float,
    targetValue: Float,
    durationMillis: Int,
    repeatMode: RepeatMode = RepeatMode.Restart
): State<Float> {
    val infiniteTransition = rememberInfiniteTransition()
    return infiniteTransition.animateFloat(
        initialValue = initialValue,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = repeatMode
        )
    )
}

@Composable
fun colorTransition(
    initialValue: Color,
    targetValue: Color,
    durationMillis: Int,
    repeatMode: RepeatMode = RepeatMode.Restart
): State<Color> {
    val infiniteTransition = rememberInfiniteTransition()
    return infiniteTransition.animateColor(
        initialValue = initialValue,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = FastOutSlowInEasing),
            repeatMode = repeatMode
        )
    )
}