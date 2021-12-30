package com.jesen.videodetail_model.ui.page

import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jesen.biliexoplayer.player.ExoComposePlayer
import com.jesen.common_util_lib.utils.LocalMainActivity
import com.jesen.retrofit_lib.model.VideoDetailM
import com.jesen.retrofit_lib.model.VideoM
import com.jesen.retrofit_lib.response.DataState
import com.jesen.videodetail_model.ui.widget.CommentListContent
import com.jesen.videodetail_model.ui.widget.TabSelectBar
import com.jesen.videodetail_model.ui.widget.VideoDesContent
import com.jesen.videodetail_model.viewmodel.DetailViewModel
import soup.compose.material.motion.MaterialFadeThrough

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
fun VideoDetailScreen(
    videoMjs: String,
    itemCardClick: (VideoM) -> Unit,
    onBackCall: () -> Unit
) {
    val activity = LocalMainActivity.current
    val viewModel by activity.viewModels<DetailViewModel>()

    val videoDetailData by viewModel.videoDetailState.collectAsState()

    // 判断视频是否加载了
    fun isVideoLoaded() = videoDetailData is DataState.Success

    // 加载视频信息
    LaunchedEffect(Unit) {
        if (!isVideoLoaded()) {
            viewModel.loadVideoInfo2(videoMjs)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsWithImePadding()
    ) {
        // 布入播放器
        ExoComposePlayer(
            activity = LocalMainActivity.current,
            modifier = Modifier.fillMaxWidth(),
            title = viewModel.curVideoM?.title ?: "",
            url = viewModel.curVideoM?.url ?: "",
            onBackCall = onBackCall,
        )

        MaterialFadeThrough(
            targetState = videoDetailData
        ) { dataStoreData ->
            when (dataStoreData) {
                is DataState.Empty,
                is DataState.Loading -> {
                    // 详情数据加载中
                    LottieDetailLoading()
                }
                is DataState.Success -> {
                    // 展示详情内容
                    DetailPageInfo(viewModel, dataStoreData.read(), itemCardClick)
                }
                is DataState.Error -> {
                    DetailLoadError(viewModel = viewModel, videoId = viewModel.curVideoM?.vid ?: "")
                }
            }
        }

    }
}

/**
 * 详情页布局
 * */
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalPagerApi
@Composable
private fun DetailPageInfo(
    viewModel: DetailViewModel,
    videoDetail: VideoDetailM,
    itemCardClick: (VideoM) -> Unit
) {
    val pagerState = rememberPagerState(0)
    val coroutineScope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize()) {

        // 顶部导航tab
        TabSelectBar(pagerState = pagerState, scope = coroutineScope, detailData = videoDetail.data)

        // tab 对应的Pager
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth(),
            state = pagerState,
            count = 2
        ) {
            when (it) {
                0 -> VideoDesContent(
                    coroutineScope = coroutineScope,
                    itemCardClick = itemCardClick,
                    viewModel = viewModel,
                    detailData = videoDetail.data
                )
                1 -> CommentListContent(pagerState = pagerState, viewModel = viewModel)
            }
        }

    }
}