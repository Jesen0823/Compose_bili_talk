package com.jesen.compose_bili.ui.pages.mainchildren

import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.jesen.common_util_lib.utils.LocalMainActivity
import com.jesen.common_util_lib.utils.LocalNavController
import com.jesen.common_util_lib.utils.oLog
import com.jesen.compose_bili.R
import com.jesen.compose_bili.navigation.NavUtil
import com.jesen.compose_bili.navigation.PageRoute
import com.jesen.compose_bili.ui.theme.bili_50
import com.jesen.compose_bili.ui.theme.gray100
import com.jesen.compose_bili.ui.theme.gray50
import com.jesen.compose_bili.ui.theme.gray700
import com.jesen.compose_bili.ui.widget.RefreshColumnScreen
import com.jesen.compose_bili.viewmodel.RankingViewModel
import com.jesen.retrofit_lib.model.videoModel2Js
import kotlinx.coroutines.launch

/**
 * 排行榜
 * */
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun RankingPage() {
    val activity = LocalMainActivity.current
    val viewModel by activity.viewModels<RankingViewModel>()

    val pagerState = rememberPagerState(
        initialPage = 0 //初始页面
    )
    val tabTitles = listOf(
        stringResource(id = R.string.rank_tab_new),
        stringResource(id = R.string.rank_tab_hot),
        stringResource(id = R.string.rank_tab_fav)
    )

    val tabState = remember {
        mutableStateOf(tabTitles[0])
    }
    val navController = LocalNavController.current
    Scaffold {
        Column(modifier = Modifier.fillMaxSize()) {
            // 顶部导航
            TopTab(pagerState = pagerState, tabTitles = tabTitles, viewModel = viewModel)

            // 横向Pager类似PagerView
            HorizontalPager(
                state = pagerState,
                count = tabTitles.size,
                reverseLayout = false
            ) { indexPage ->
                oLog("home switch page: pageIndex = $indexPage")
                oLog("home currentPage: pageIndex = ${pagerState.currentPage}")
                // 以下是pager的具体内容
                RefreshColumnScreen(
                    indexPage = indexPage,
                    viewModel = viewModel,
                    onItemClick = {
                        NavUtil.doPageNavigationTo(
                            navController = navController,
                            PageRoute.VIDEO_DETAIL_ROUTE.replaceAfter("=", videoModel2Js(it))
                        )
                    },
                    pagingDataList = viewModel.rankListData[indexPage].second
                )
            }
        }
        tabState.value = tabTitles[pagerState.currentPage]
    }
}

// 不可滑动TabView
@ExperimentalPagerApi
@Composable
fun TopTab(pagerState: PagerState, tabTitles: List<String>, viewModel: RankingViewModel) {
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = Modifier.wrapContentWidth(),
        // 默认指示器
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(
                    tabPositions[pagerState.currentPage]
                ),
                color = bili_50
            )
        },
        backgroundColor = gray100,
        divider = {
            TabRowDefaults.Divider(color = Color.White)
        }
    ) {
        tabTitles.forEachIndexed { index, title ->

            Tab(
                modifier = Modifier.background(gray50),
                text = {
                    Text(
                        text = title,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Thin
                        )
                    )
                },
                selected = index == pagerState.currentPage,
                unselectedContentColor = gray700,
                selectedContentColor = bili_50,
                onClick = {
                    scope.launch {
                        if (viewModel.selectedIndex == index) {
                            oLog("click tab current Item is same. index: $index")
                            viewModel.lazyColumnStateList[index].animateScrollToItem(0)
                        } else {
                            pagerState.scrollToPage(index)
                            viewModel.selectedIndex = index
                        }
                    }
                }
            )
        }
    }
}
