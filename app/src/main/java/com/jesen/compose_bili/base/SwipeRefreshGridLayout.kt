package com.jesen.compose_bili.base

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.jesen.bilibanner.bean.BannerData
import com.jesen.compose_bili.ui.widget.BiliBanner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 下拉加载封装 LazyVerticalGrid
 *
 * implementation "com.google.accompanist:accompanist-swiperefresh:xxx"
 * */
@ExperimentalPagerApi
@ExperimentalFoundationApi
@Composable
fun <T : Any> SwipeRefreshGridLayout(
    lazyListState: LazyListState,
    collectAsLazyPagingItems: LazyPagingItems<T>,
    listContent: LazyGridScope.() -> Unit,
) {
    val rememberSwipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    var loadingPageError by remember { mutableStateOf(false) }

    SwipeRefresh(
        state = rememberSwipeRefreshState,
        onRefresh = { collectAsLazyPagingItems.refresh() }
    ) {

        val coroutineScope = rememberCoroutineScope()

        rememberSwipeRefreshState.isRefreshing =
            collectAsLazyPagingItems.loadState.refresh is LoadState.Loading

        if (loadingPageError) {
            ErrorContent() {
                collectAsLazyPagingItems.retry()
            }
        } else {
            LazyVerticalGrid(
                cells = GridCells.Fixed(2),
                state = lazyListState,
                contentPadding = PaddingValues(start = 5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),

                ) {
                listContent()
                handlerLoadState(
                    pagingItems = collectAsLazyPagingItems,
                    contextGrid = this,
                    scope = coroutineScope,
                    lazyListState = lazyListState,
                )
            }
        }
    }
}

@ExperimentalFoundationApi
fun <T : Any> handlerLoadState(
    pagingItems: LazyPagingItems<T>,
    contextGrid: LazyGridScope? = null,
    contextColumn: LazyListScope? = null,
    scope: CoroutineScope,
    lazyListState: LazyListState,
) {
    pagingItems.apply {
        when {
            loadState.append is LoadState.Loading -> {
                //加载更多，底部loading
                contextGrid?.item {
                    LoadingItem()
                }
            }
            loadState.append is LoadState.Error -> {
                //加载更多异常
                contextGrid?.item {
                    ErrorMoreRetryItem() {
                        pagingItems.retry()
                    }
                }
            }

            loadState.append == LoadState.NotLoading(endOfPaginationReached = true) -> {
                // 已经没有更多数据了
                contextGrid?.item {
                    NoMoreDataFindItem(onClick = {
                        scope.launch {
                            lazyListState.animateScrollToItem(0)
                        }
                    })
                }
            }

            loadState.refresh is LoadState.Error -> {
                if (pagingItems.itemCount <= 0) {
                    //刷新的时候，如果itemCount小于0，第一次加载异常
                    //loadingPageError = true
                } else {
                    contextGrid?.item {
                        ErrorMoreRetryItem() {
                            pagingItems.retry()
                        }
                    }
                }
            }
            loadState.refresh is LoadState.Loading -> {
                // 第一次加载且正在加载中
                if (pagingItems.itemCount == 0) {
                }
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
fun extendContent(modifier: Modifier, bannerDataList: MutableList<BannerData>?) {
    bannerDataList?.let {
        BiliBanner(
            modifier = modifier.fillMaxWidth(),
            items = it,
            itemOnClick = { banner ->
                // 点击banner
            }
        )
    }
}

