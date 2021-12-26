package com.jesen.qrzxing.compose

import androidx.compose.animation.core.RepeatMode
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jesen.qrzxing.*

/**
 * 简单自定义扫描动效
 * */
@Composable
fun ScanDecoration(modifier: Modifier) {
    val duration = 1500
    Box(modifier = modifier) {
        var borderColor by remember { mutableStateOf(primaryDeepColor) }
        BoxWithConstraints(
            modifier = modifier
                .padding(horizontal = 64.dp, vertical = 100.dp)
                .border(width = 8.dp, color = borderColor, shape = RoundedCornerShape(24.dp))
        ) {

            val endY = maxHeight

            val progress = floatTransition(
                initialValue = 0.1f,
                targetValue = 1f,
                durationMillis = duration,
                repeatMode = RepeatMode.Reverse
            )
            borderColor = colorTransition(
                initialValue = primaryDeepColor,
                targetValue = goldenrod,
                durationMillis = duration
            ).value

            val lineAlpha =
                if (progress.value < 0.5f) progress.value * 2 else (1 - progress.value) * 2
            Box(
                modifier = Modifier
                    .offset(x = 0.dp, y = (endY.value.dp * progress.value))
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(4.dp)
                    .background(
                        color = primaryColor.copy(alpha = lineAlpha),
                        shape = RoundedCornerShape(8.dp),
                    )
                    .advancedShadow(
                        color = Color.White,
                        alpha = 0.9f,
                        cornersRadius = 12.dp,
                        shadowBlurRadius = 5.dp,
                        offsetY = 6.dp,
                        offsetX = 2.dp
                    )
            )
        }
    }
}