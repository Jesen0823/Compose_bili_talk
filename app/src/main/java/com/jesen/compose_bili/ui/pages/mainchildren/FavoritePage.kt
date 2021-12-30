package com.jesen.compose_bili.ui.pages.mainchildren

import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.common_util_lib.paging.SwipeRefreshColumnLayout
import com.jesen.common_util_lib.utils.LocalMainActivity
import com.jesen.common_util_lib.utils.LocalNavController
import com.jesen.compose_bili.navigation.NavUtil
import com.jesen.compose_bili.navigation.PageRoute
import com.jesen.compose_bili.ui.theme.gray600
import com.jesen.compose_bili.viewmodel.FavoriteListViewModel
import com.jesen.retrofit_lib.model.videoModel2Js
import com.jesen.videodetail_model.util.SmallVideoCard
import kotlinx.coroutines.launch

/**
 * 收藏列表页面
 * */
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun FavoritePage() {
    val activity = LocalMainActivity.current
    val viewModel by activity.viewModels<FavoriteListViewModel>()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 10.dp, horizontal = 23.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "收藏",
                            fontSize = 18.sp,
                            color = MaterialTheme.colors.onSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                backgroundColor = Color.White,
                contentColor = gray600,
                elevation = 10.dp
            )
        }
    ) {

        val coroutineScope = rememberCoroutineScope()
        val navController = LocalNavController.current
        val lazyPagingItems = viewModel.favoriteListData.collectAsLazyPagingItems()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp)
        ) {
            val columnState = rememberLazyListState()

            SwipeRefreshColumnLayout(
                columnState = columnState,
                collectAsLazyPagingItems = lazyPagingItems,
            ) {

                items(count = lazyPagingItems.itemCount,
                    key = { index ->
                        index
                    }
                ) { index ->
                    lazyPagingItems[index]?.let { video ->
                        SmallVideoCard(
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
            }
        }
    }
}