package com.jesen.compose_bili.ui.widget

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import com.jesen.compose_bili.base.SwipeRefreshColumnLayout
import com.jesen.compose_bili.viewmodel.RankingViewModel
import com.jesen.retrofit_lib.model.VideoM
import com.jesen.videodetail_model.util.SmallVideoCard
import kotlinx.coroutines.flow.Flow

/**
 * 列表Item的装载 ---下拉刷新，加载更多动效
 * */
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun RefreshColumnScreen(
    indexPage: Int,
    viewModel: RankingViewModel,
    context: Context,
    pagingDataList: Flow<PagingData<VideoM>>,
) {
    val collectAsLazyPagingItems = pagingDataList.collectAsLazyPagingItems()
    val columnState = rememberLazyListState()
    // 存储列表状态供外部使用
    viewModel.lazyColumnStateList.add(indexPage, columnState)

    SwipeRefreshColumnLayout(
        columnState = columnState,
        collectAsLazyPagingItems = collectAsLazyPagingItems,
    ) {

        items(count = collectAsLazyPagingItems.itemCount,
            key = { index ->
                index
            }
        ) { index ->
            collectAsLazyPagingItems[index]?.let {
                SmallVideoCard(
                    video = it,
                    onClick = { Toast.makeText(context, "ccc", Toast.LENGTH_SHORT).show() },
                )
            }
        }
    }
}
