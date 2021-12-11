package com.jesen.compose_bili.ui.pages.mainchildren

import android.os.Build
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.jesen.compose_bili.MainActivity
import com.jesen.compose_bili.model.CategoryM
import com.jesen.compose_bili.navigation.PageRoute
import com.jesen.compose_bili.navigation.doPageNavigationTo
import com.jesen.compose_bili.ui.theme.bili_90
import com.jesen.compose_bili.ui.theme.gray100
import com.jesen.compose_bili.ui.theme.gray50
import com.jesen.compose_bili.ui.theme.gray700
import com.jesen.compose_bili.ui.widget.BiliAnimatedIndicator
import com.jesen.compose_bili.ui.widget.MainTopBarUI
import com.jesen.compose_bili.ui.widget.RefreshCategoryContentScreen
import com.jesen.compose_bili.utils.ColorUtil
import com.jesen.compose_bili.utils.oLog
import com.jesen.compose_bili.utils.replaceRegex
import com.jesen.compose_bili.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

/**
 * 首页 HorizontalPager 和 TabRow的使用
 * ScrollableTabRow: 可滑动的 TabView
 *
 * implement 'com.google.accompanist:accompanist-pager:0.18.0'
 *
 * */

@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
fun HomeTabPage(activity: MainActivity) {

    val viewModel by activity.viewModels<HomeViewModel>()

    val tabState = remember {
        mutableStateOf(viewModel.categoryList[0])
    }

    val scope = rememberCoroutineScope()
    var indicatorState by remember { mutableStateOf(0) }

    val pagerState = rememberPagerState(
        initialPage = 0 //初始页面
    )

    Scaffold(
        topBar = {
            MainTopBarUI(
                {
                    scope.launch {
                        doPageNavigationTo(replaceRegex(PageRoute.VIDEO_DETAIL_ROUTE, "7688021"))
                    }

                }, {}, {})
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            val categoryList = viewModel.categoryList
            Column {

                // 顶部导航
                ScrollableTab(pagerState, categoryList)

                // 横向Pager类似PagerView
                HorizontalPager(
                    state = pagerState,
                    count = viewModel.categoryList.size,
                    reverseLayout = false
                ) { indexPage ->
                    oLog("home switch page: pageIndex = $indexPage")
                    oLog("home currentPage: pageIndex = ${pagerState.currentPage}")
                    // 以下是pager的具体内容
                    RefreshCategoryContentScreen(
                        viewModel = viewModel,
                        context = activity,
                        index = pagerState.currentPage
                    )
                }
            }
            tabState.value = viewModel.categoryList[pagerState.currentPage]
        }
    }
}

// 可滑动TabView
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalPagerApi
@Composable
fun ScrollableTab(pagerState: PagerState, categoryList: MutableList<CategoryM>) {
    val scope = rememberCoroutineScope()

    ScrollableTabRow(
        //selectedTabIndex = viewModel.categoryList.indexOf(tabState.value),
        selectedTabIndex = pagerState.currentPage,
        modifier = Modifier.wrapContentWidth(),
        edgePadding = 16.dp,
        // 默认指示器
        /*indicator = { tabIndicator ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(
                    tabIndicator[categoryList.indexOf(
                        tabstr.value
                    )]
                ),
                color = Color.Cyan
            )
        },*/
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
            // val selected = index == viewModel.categoryList.indexOf(tabState.value)
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
                selectedContentColor = ColorUtil.getRandomColorB(bili_90),
                onClick = {
                    //tabState.value = viewModel.categoryList[index]
                    scope.launch {
                        pagerState.scrollToPage(index)
                    }
                }
            )
        }
    }
}
