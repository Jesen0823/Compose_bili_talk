package com.jesen.compose_bili.ui.widget

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import com.jesen.compose_bili.base.SwipeRefreshGridList
import com.jesen.compose_bili.utils.oLog
import com.jesen.compose_bili.viewmodel.HomeViewModel

/**
 * 首页列表加载 ---下拉刷新，加载更多动效
 * */
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun RefreshCategoryContentScreen(
    viewModel: HomeViewModel,
    context: Context,
    index: Int,
) {

    oLog(" refresh content index = $index")

    val collectAsLazyPagingIDataList =
        viewModel.getVideoListIndexOf(index).collectAsLazyPagingItems()

    SwipeRefreshGridList(
        collectAsLazyPagingItems = collectAsLazyPagingIDataList
    ) {

        items(collectAsLazyPagingIDataList.itemCount) { index ->
            collectAsLazyPagingIDataList[index]?.let {
                VideoItemCard(
                    index = index,
                    video = it,
                    onClick = { Toast.makeText(context, "ccc", Toast.LENGTH_SHORT).show() },
                )
            }
        }
    }
}
