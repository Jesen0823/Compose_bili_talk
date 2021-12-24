package com.jesen.videodetail_model.ui.widget

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.jesen.common_util_lib.paging.SwipeRefreshColumnLayout
import com.jesen.common_util_lib.utils.showToast
import com.jesen.videodetail_model.viewmodel.DetailViewModel

@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
fun CommentListContent(viewModel: DetailViewModel, pagerState: PagerState) {

    if (pagerState.currentPage == 1) {

        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            val columnState = rememberLazyListState()

            val lazyPagingItems = viewModel.getCommentListData.collectAsLazyPagingItems()

            SwipeRefreshColumnLayout(
                columnState = columnState,
                collectAsLazyPagingItems = lazyPagingItems,
            ) {

                items(count = lazyPagingItems.itemCount,
                    key = { index ->
                        index
                    }
                ) { index ->
                    lazyPagingItems[index]?.let {
                        CommentItemUI(comment = it, viewModel = viewModel)
                    }
                }
            }

            val context = LocalContext.current
            // 底部输入框
            CommentBottomInput(
                modifier = Modifier.align(Alignment.BottomCenter),
                onSend = {
                    // 发起评论
                    viewModel.addNewComment(it) { ret ->
                        ret?.let {
                            if (it.status == 200) {
                                // 成功则刷新
                                lazyPagingItems.refresh()
                            }
                            showToast(context, ret.message)
                        }
                    }
                }
            )

        }
    }
}