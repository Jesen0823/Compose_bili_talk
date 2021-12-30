package com.jesen.compose_bili.ui.pages.delegate

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.common_util_lib.utils.LocalNavController
import com.jesen.compose_bili.navigation.NavUtil
import com.jesen.compose_bili.navigation.PageRoute
import com.jesen.retrofit_lib.model.videoModel2Js
import com.jesen.videodetail_model.ui.page.VideoDetailScreen
import kotlinx.coroutines.launch

/**
 * 视频详情页
 */
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun DelegateVideoDetail(
    videoJs: String,
) {
    val navController = LocalNavController.current
    val coroutineScope = rememberCoroutineScope()
    VideoDetailScreen(
        videoMjs = videoJs,
        itemCardClick = { video ->
            coroutineScope.launch {
                NavUtil.doPageNavigationTo(
                    navController = navController,
                    PageRoute.VIDEO_DETAIL_ROUTE.replaceAfter("=", videoModel2Js(video))
                )
            }
        },
        onBackCall = {
            navController.popBackStack()
        }
    )
}