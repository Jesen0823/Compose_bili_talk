package com.jesen.compose_bili.ui.widget

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.common_util_lib.paging.SwipeRefreshColumnLayout
import com.jesen.common_util_lib.paging.SwipeRefreshGridLayout
import com.jesen.common_util_lib.utils.LocalNavController
import com.jesen.common_util_lib.utils.oLog
import com.jesen.compose_bili.navigation.NavUtil
import com.jesen.compose_bili.navigation.PageRoute
import com.jesen.retrofit_lib.model.VideoM
import com.jesen.retrofit_lib.model.videoModel2Js
import kotlinx.coroutines.launch

/**
 * 首页列表加载 ---下拉刷新，加载更多动效
 * */
@ExperimentalAnimationApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun RefreshCategoryContentScreen(
    context: Context,
    index: Int,
    videoCategoryList: LazyPagingItems<VideoM>,
    extendItemUI: LazyListScope.() -> Unit,
) {

    oLog(" refresh content index = $index")

    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val navController = LocalNavController.current

    /**
     * index = 0 含Banner，不使用LazyVerticalGrid
     * 利用 LazyColumn 实现网格和Banner混合布局
     *
     * */
    if (index == 0) {
        SwipeRefreshColumnLayout(
            columnState = lazyListState,
            collectAsLazyPagingItems = videoCategoryList,
        ) {

            extendItemUI()

            // 需要展示几栏
            val columnSize = 2
            // rows 总共几行
            val rowSize = (videoCategoryList.itemCount + columnSize - 1) / columnSize
            items(rowSize) { rowIndex ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (columnIndex in 0 until columnSize) {
                        //itemIndex List数据位置
                        val itemIndex = rowIndex * columnSize + columnIndex
                        if (itemIndex < videoCategoryList.itemCount) {
                            Box(
                                modifier = Modifier
                                    .weight(1f, fill = true)
                                    .padding(
                                        start = if (itemIndex % 2 == 0) 0.dp else 2.dp,
                                        end = if (itemIndex % 2 == 0) 2.dp else 0.dp
                                    ),
                                propagateMinConstraints = true
                            ) {
                                videoCategoryList[itemIndex]?.let { video ->
                                    VideoItemCard(
                                        video = video,
                                        onClick = {
                                            coroutineScope.launch {
                                                NavUtil.doPageNavigationTo(
                                                    navController = navController,
                                                    PageRoute.VIDEO_DETAIL_ROUTE.replaceAfter(
                                                        "=",
                                                        videoModel2Js(video)
                                                    )
                                                )
                                            }
                                        },
                                    )
                                }
                            }
                        } else {
                            Spacer(Modifier.weight(1f, fill = true))
                        }
                    }
                }
            }
        }
    } else {
        // 除了推荐外，一律用LazyVerticalGrid,不用再计算行列数
        SwipeRefreshGridLayout(
            lazyListState = lazyListState,
            collectAsLazyPagingItems = videoCategoryList,
        ) {
            items(videoCategoryList.itemCount) { index ->
                videoCategoryList[index]?.let {
                    VideoItemCard(
                        video = it,
                        onClick = {
                            coroutineScope.launch {
                                NavUtil.doPageNavigationTo(
                                    navController = navController,
                                    PageRoute.VIDEO_DETAIL_ROUTE.replaceAfter(
                                        "=",
                                        videoModel2Js(it)
                                    )
                                )
                            }
                        },
                    )
                }
            }
        }
    }
}
