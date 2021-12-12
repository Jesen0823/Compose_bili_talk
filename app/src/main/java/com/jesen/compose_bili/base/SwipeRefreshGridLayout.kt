package com.jesen.compose_bili.base

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyGridScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.jesen.bilibanner.bean.BannerData
import com.jesen.compose_bili.ui.widget.BiliBanner
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
    modifier: Modifier,
    lazyListState: LazyListState,
    collectAsLazyPagingItems: LazyPagingItems<T>,
    bannerList: MutableList<BannerData>? = null,
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
            ConstraintLayout(modifier = modifier.fillMaxSize()) {

                val (extendUI, lazyGridUI) = createRefs()
                extendContent(
                    modifier = Modifier.constrainAs(extendUI) {
                        centerHorizontallyTo(parent)
                        top.linkTo(parent.top)
                    },
                    bannerDataList = bannerList
                )

                LazyVerticalGrid(
                    cells = GridCells.Fixed(2),
                    state = lazyListState,
                    contentPadding = PaddingValues(start = 5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .constrainAs(lazyGridUI) {
                            centerHorizontallyTo(parent)
                            top.linkTo(extendUI.bottom)
                            bottom.linkTo(parent.bottom)
                        },

                    ) {
                    listContent()
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
                                // 已经没有更多数据了
                                item {
                                    NoMoreDataFindItem(onClick = {
                                        coroutineScope.launch {
                                            lazyListState.animateScrollToItem(0)
                                        }
                                    })
                                }
                            }

                            loadState.refresh is LoadState.Error -> {
                                if (collectAsLazyPagingItems.itemCount <= 0) {
                                    //刷新的时候，如果itemCount小于0，第一次加载异常
                                    loadingPageError = true
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


