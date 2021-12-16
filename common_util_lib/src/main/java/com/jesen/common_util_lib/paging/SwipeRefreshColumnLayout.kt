package com.jesen.common_util_lib.paging

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * 下拉加载封装
 *
 * implementation "com.google.accompanist:accompanist-swiperefresh:xxx"
 * */
@Composable
fun <T : Any> SwipeRefreshColumnLayout(
    columnState: LazyListState = rememberLazyListState(),
    collectAsLazyPagingItems: LazyPagingItems<T>,
    listContent: LazyListScope.() -> Unit,
) {

    val rememberSwipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = collectAsLazyPagingItems.loadState.refresh == LoadState.Loading
    )

    when (collectAsLazyPagingItems.loadState.refresh) {
        // 页面加载失败，第一屏失败
        is LoadState.Error -> ErrorContent() {
            collectAsLazyPagingItems.retry()
        }
        else -> {
            SwipeRefresh(
                state = rememberSwipeRefreshState,
                onRefresh = { collectAsLazyPagingItems.refresh() }
            ) {
                LazyColumn(
                    state = columnState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),

                    ) {
                    listContent()

                    // 监听状态
                    collectAsLazyPagingItems.apply {
                        when {
                            loadState.append is LoadState.Loading -> {
                                //加载更多，底部loading
                                item { LoadingItem() }
                            }
                            loadState.append is LoadState.Error -> {
                                //加载更多异常
                                item {
                                    ErrorMoreRetryItem() {
                                        collectAsLazyPagingItems.retry()
                                    }
                                }
                            }
                            loadState.append == LoadState.NotLoading(endOfPaginationReached = true) -> {
                                // 没有更多数据了
                                item { NoMoreDataFindItem {} }
                            }
                            loadState.refresh is LoadState.Error -> {
                                if (collectAsLazyPagingItems.itemCount <= 0) {
                                    //刷新的时候，如果itemCount小于0，第一次加载异常
                                    /*item {}*/
                                } else {
                                    item {
                                        ErrorMoreRetryItem() {
                                            collectAsLazyPagingItems.retry()
                                        }
                                    }
                                }
                            }
                            loadState.refresh is LoadState.Loading -> {
                                // 第一次加载且正在加载中
                                if (collectAsLazyPagingItems.itemCount == 0) {
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
