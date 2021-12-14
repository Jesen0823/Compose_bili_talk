package com.jesen.videodetail_model.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.jesen.videodetail_model.R
import com.jesen.videodetail_model.viewmodel.DetailViewModel

/**
 * 详情页加载动画
 * */
@Composable
fun LottieDetailLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(
                R.raw.detail_loading_lottie
            )
        )
        LottieAnimation(
            modifier = Modifier.size(360.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever,
        )
    }
}

/**
 * 详情页加载失败
 * */
@Composable
fun DetailLoadError(viewModel: DetailViewModel, videoId: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { viewModel.loadVideoInfo2(videoId) },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .padding(10.dp)
                    .clip(CircleShape)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.load_err),
                    contentDescription = null
                )
            }
            Text(text = "加载失败，点击重试~ ", fontWeight = FontWeight.Bold)
        }
    }
}