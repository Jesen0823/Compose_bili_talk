package com.jesen.bilibanner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.jesen.bilibanner.bean.BannerData
import kotlinx.coroutines.launch
import java.util.*

/**
 * 参考： https://github.com/zhujiang521/Banner
 * */

@ExperimentalPagerApi
@Composable
fun BannerPager(
    modifier: Modifier = Modifier,
    items: List<BannerData> = arrayListOf(),
    config: BannerConfig = BannerConfig(),
    onBannerClick: (BannerData) -> Unit
) {
    if (items.isEmpty()) {
        throw NullPointerException("items is not null")
    }

    val pagerState = rememberPagerState()

    if (config.repeat) {
        StartBanner(pagerState, config.intervalTime)
    }


    Box(modifier = modifier.height(config.bannerHeight)) {
        HorizontalPager(
            count = items.size,
            state = pagerState,
        ) { page ->
            val item = items[page]
            BannerItem(
                data = item,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(config.bannerImagePadding),
                shape = config.shape,
                contentScale = config.contentScale
            ) { data ->
                onBannerClick(data)
            }
        }

        PointIndicator(
            selectIndicatorColor = config.selectedColor,
            indicatorColor = config.indicatorColor,
        ).DrawIndicator(pagerState)
    }
}

data class BannerConfig(
    // banner 高度
    var bannerHeight: Dp = 210.dp,
    // banner 图片距离四周的 padding 值
    var bannerImagePadding: Dp = 8.dp,
    // banner 图片的 shape
    var shape: Shape = RoundedCornerShape(10.dp),
    // banner 切换间隔时间
    var intervalTime: Long = 3000,
    // 使用可选的scale参数来确定要使用的纵横比缩放
    var contentScale: ContentScale = ContentScale.Crop,
    // 是否循环播放
    var repeat: Boolean = true,
    var indicatorColor: Color = Color.Gray.copy(alpha = 0.7f),
    var selectedColor: Color = Color.White
)

var mTimer: Timer? = null
var mTimerTask: TimerTask? = null


@ExperimentalPagerApi
@Composable
fun StartBanner(pagerState: PagerState, intervalTime: Long) {
    val coroutineScope = rememberCoroutineScope()
    mTimer?.cancel()
    mTimerTask?.cancel()
    mTimer = Timer()
    mTimerTask = object : TimerTask() {
        override fun run() {
            coroutineScope.launch {
                pagerState.animateScrollToPage((pagerState.currentPage + 1) % pagerState.pageCount)
            }
        }
    }
    mTimer?.schedule(mTimerTask, intervalTime, intervalTime)
}