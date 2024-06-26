package com.jesen.compose_bili.ui.pages.mainchildren

import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.jesen.bilibanner.BannerConfig
import com.jesen.common_util_lib.utils.ColorUtil
import com.jesen.common_util_lib.utils.LocalMainActivity
import com.jesen.common_util_lib.utils.LocalNavController
import com.jesen.common_util_lib.utils.oLog
import com.jesen.compose_bili.navigation.NavUtil
import com.jesen.compose_bili.navigation.PageRoute
import com.jesen.compose_bili.ui.theme.bili_90
import com.jesen.compose_bili.ui.theme.gray100
import com.jesen.compose_bili.ui.theme.gray50
import com.jesen.compose_bili.ui.theme.gray700
import com.jesen.compose_bili.ui.widget.BiliAnimatedIndicator
import com.jesen.compose_bili.ui.widget.BiliBanner
import com.jesen.compose_bili.ui.widget.MainTopBarUI
import com.jesen.compose_bili.ui.widget.RefreshCategoryContentScreen
import com.jesen.compose_bili.utils.replaceRegex
import com.jesen.compose_bili.viewmodel.HomeViewModel
import com.jesen.retrofit_lib.model.CategoryM
import com.jesen.retrofit_lib.model.VideoM
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * 首页 HorizontalPager 和 TabRow的使用
 * ScrollableTabRow: 可滑动的 TabView
 *
 * API特征： 请求参数category == "推荐" 会返回categoryList和bannerList
 *
 * implement 'com.google.accompanist:accompanist-pager:0.18.0'
 *
 * */


@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
fun HomeTabPage(categoryIndex: Int) {
    val activity = LocalMainActivity.current
    val viewModel by activity.viewModels<HomeViewModel>()

    val tabState = remember {
        mutableStateOf(viewModel.categoryList[0])
    }

    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = categoryIndex //初始页面
    )

    val navController = LocalNavController.current

    Scaffold(
        topBar = {
            MainTopBarUI(
                {
                    scope.launch {
                        NavUtil.doPageNavigationTo(navController, PageRoute.SEARCH_ROUTE)
                    }
                    scope.run {
                        NavUtil.doPageNavigationTo(navController, PageRoute.SEARCH_ROUTE)
                    }
                }, {
                    scope.launch {
                        NavUtil.doPageNavigationTo(navController,replaceRegex(PageRoute.VIDEO_DETAIL_ROUTE, "7688021"))
                    }
                    //scope.run { NavUtil.doPageNavigationTo(navController,replaceRegex(PageRoute.VIDEO_DETAIL_ROUTE, "7688021")) }
                }, {
                    scope.launch {
                        NavUtil.doPageNavigationTo(navController,PageRoute.NOTICE_ROUTE)
                    }
                    //scope.run { NavUtil.doPageNavigationTo(navController,PageRoute.NOTICE_ROUTE) }
                }
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // 顶部导航
            ScrollableTab(pagerState, viewModel.categoryList)

            // 横向Pager类似PagerView
            HorizontalPager(
                state = pagerState,
                count = viewModel.categoryList.size,
                reverseLayout = false
            ) { indexPage ->
                oLog("home switch page: pageIndex = $indexPage")
                oLog("home currentPage: pageIndex = ${pagerState.currentPage}")

                viewModel.selectedIndex = indexPage

                // 按栏目获取数据，可以简单做个viewModel缓存，避免跳转的时候去重新请求
                val curCategory = viewModel.categoryList[indexPage]
                val videoPagingDataList: Pair<CategoryM, Flow<PagingData<VideoM>>> =
                    if (viewModel.allCategoryVideoMap != null && viewModel.allCategoryVideoMap?.contains(
                            curCategory
                        ) == true
                    ) {
                        viewModel.allCategoryVideoMap?.get(curCategory)!!
                    } else {
                        val newData =
                            viewModel.getVideoPagingDataList(viewModel.categoryList[indexPage])
                        viewModel.allCategoryVideoMap?.put(curCategory, newData)
                        newData
                    }

                // 以下是pager的具体内容
                RefreshCategoryContentScreen(
                    context = activity,
                    index = pagerState.currentPage,
                    videoCategoryList = videoPagingDataList.second.collectAsLazyPagingItems(),
                    // 可扩展位置，只有含banner的栏目才有
                    extendItemUI = {
                        val context = this
                        if (indexPage == 0) {
                            context.item {
                                BiliBanner(
                                    modifier = Modifier.fillMaxWidth(),
                                    items = viewModel.bannerDataList,
                                    config = BannerConfig(
                                        indicatorColor = Color.Gray.copy(0.8f),
                                        selectedColor = Color.White.copy(0.8f),
                                        intervalTime = 3000
                                    ),

                                    itemOnClick = { banner ->
                                        // 点击banner
                                        scope.launch {
                                            NavUtil.doPageNavigationTo(
                                                navController,
                                                PageRoute.WEB_VIEW_ROUTE.replaceAfter(
                                                    "=",
                                                    banner.url
                                                )
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                )
            }

            tabState.value = viewModel.categoryList[pagerState.currentPage]
        }
    }
}

// 可滑动TabView
@ExperimentalPagerApi
@Composable
fun ScrollableTab(pagerState: PagerState, categoryList: MutableList<CategoryM>) {
    val scope = rememberCoroutineScope()

    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = Modifier.wrapContentWidth(),
        edgePadding = 16.dp,

        // 自定义指示器
        indicator = @Composable { tabPositions: List<TabPosition> ->
            BiliAnimatedIndicator(
                tabPositions = tabPositions,
                selectedTabIndex = pagerState.currentPage
            )
        },
        backgroundColor = gray100,
        divider = {
            TabRowDefaults.Divider(color = Color.White)
        }
    ) {
        categoryList.forEachIndexed { index, category ->
            val selected = index == pagerState.currentPage

            Tab(
                modifier = Modifier.background(gray50),
                text = {
                    Text(
                        text = category.name,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Thin
                        )
                    )
                },
                selected = selected,
                unselectedContentColor = gray700,
                selectedContentColor = ColorUtil.getRandomColor(bili_90),
                onClick = {
                    scope.launch {
                        pagerState.scrollToPage(index)
                    }
                }
            )
        }
    }
}
