package com.jesen.compose_bili.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.jesen.compose_bili.R
import com.jesen.compose_bili.ui.theme.gray300
import com.jesen.compose_bili.ui.theme.gray700

/**
 * 加载动画
 * */
@Composable
fun LoadingLottieUI(message: String) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .size(120.dp)
                .background(color = gray300.copy(alpha = 0.8f), shape = RoundedCornerShape(6.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

            ) {

            // 加载文件
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lolitte_circle_loading))
            val progress by animateLottieCompositionAsState(
                composition = composition,
                // 一直播放
                iterations = LottieConstants.IterateForever
            )
            LottieAnimation(
                modifier = Modifier.size(80.dp),
                composition = composition,
                progress = progress
            )

            Text(
                text = message,
                style = TextStyle(fontSize = 22.sp, textAlign = TextAlign.Center, color = gray700)
            )
        }
    }
}

