package com.jesen.qrzxing.debug

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp
import com.jesen.qrzxing.compose.advancedShadow

@Composable
fun ZxingDrawTestPage(
) {

    Scaffold() {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(16.dp)
        ) {

            drawLine(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 16.dp)
            )
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .shadow(15.dp, CircleShape)
                    .clip(CircleShape)
                    .background(color = Color.Green)
                    .advancedShadow(
                        color = Color.Black,
                        offsetX = 2.dp,
                        offsetY = (-3).dp,
                        alpha = 0.7f
                    )
                    .clickable {

                    }
            )
        }
    }
}

@Composable
fun drawLine(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier, onDraw = {
        drawLine(
            start = Offset(0f, size.height),
            end = Offset(size.width, size.height),
            color = Color.Gray,
            strokeWidth = 6f,
            pathEffect = PathEffect.cornerPathEffect(4.0f)
        )
    })
}