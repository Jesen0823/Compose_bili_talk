package com.jesen.compose_bili.ui.widget

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import coil.annotation.ExperimentalCoilApi
import com.jesen.compose_bili.base.SwipeRefreshGridLayout
import com.jesen.compose_bili.model.VideoM
import com.jesen.compose_bili.utils.oLog

/**
 * 首页列表加载 ---下拉刷新，加载更多动效
 * */
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun RefreshCategoryContentScreen(
    context: Context,
    index: Int,
    videoCategoryList: LazyPagingItems<VideoM>,
) {

    oLog(" refresh content index = $index")

    val lazyListState = rememberLazyListState()

    SwipeRefreshGridLayout(
        lazyListState = lazyListState,
        collectAsLazyPagingItems = videoCategoryList
    ) {

        items(videoCategoryList.itemCount) { index ->
            videoCategoryList[index]?.let {
                VideoItemCard(
                    index = index,
                    video = it,
                    onClick = { Toast.makeText(context, "ccc", Toast.LENGTH_SHORT).show() },
                )
            }
        }
    }
}
