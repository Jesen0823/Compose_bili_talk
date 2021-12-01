package com.jesen.compose_bili.ui.pages.mainchildren

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jesen.compose_bili.navigation.PageRoute
import com.jesen.compose_bili.navigation.doPageNavigationTo
import com.jesen.compose_bili.ui.theme.bili_90
import com.jesen.compose_bili.ui.theme.gray100
import com.jesen.compose_bili.ui.theme.gray50
import com.jesen.compose_bili.ui.theme.gray700
import com.jesen.compose_bili.ui.widget.BiliAnimatedIndicator
import com.jesen.compose_bili.ui.widget.MainTopBarUI
import com.jesen.compose_bili.utils.ColorUtil
import com.jesen.compose_bili.utils.replaceRegex
import kotlinx.coroutines.launch

/**
 * 首页 HorizontalPager 和 TabRow的使用
 * ScrollableTabRow: 可滑动的 TabView
 *
 * implement 'com.google.accompanist:accompanist-pager:0.18.0'
 *
 * */

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
fun HomeTabPage() {
    val items = listOf("推荐", "电影", "电视剧", "综艺", "纪录片", "娱乐", "新闻")

    val tabstr = remember {
        mutableStateOf(items[0])
    }

    val scope = rememberCoroutineScope()
    var indicatorState by remember { mutableStateOf(0) }

    val state = rememberPagerState(
        //pageCount = items.size, //总页数
        //initialOffscreenLimit = 3, //预加载的个数
        //infiniteLoop = false, //是否无限循环
        initialPage = 0 //初始页面
    )
    /*val state = rememberPagerState(
        //总页数
        pageCount = items.size,
    )*/

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
            Column {
                // 可滑动TabView
                ScrollableTabRow(
                    selectedTabIndex = items.indexOf(tabstr.value),
                    modifier = Modifier.wrapContentWidth(),
                    edgePadding = 16.dp,
                    // 默认指示器
                    /*indicator = { tabIndicator ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(
                                tabIndicator[items.indexOf(
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
                            selectedTabIndex = items.indexOf(
                                tabstr.value
                            )
                        )
                    },
                    backgroundColor = gray100,
                    divider = {
                        TabRowDefaults.Divider(color = Color.Gray)
                    }
                ) {
                    items.forEachIndexed { index, title ->
                        val selected = index == items.indexOf(tabstr.value)
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
                            selected = selected,
                            unselectedContentColor = gray700,
                            selectedContentColor = ColorUtil.getRandomColorB(bili_90),
                            onClick = {
                                tabstr.value = items[index]
                                scope.launch {
                                    state.scrollToPage(index)
                                }
                            }
                        )
                    }
                }

                // 横向Pager类似PagerView
                HorizontalPager(
                    state = state,
                    count = items.size,
                    reverseLayout = false
                ) { indexPage ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (indexPage) {
                            in 0..(items.size) -> Text(text = items[indexPage])

                        }
                        Text(text = items[indexPage])
                    }
                }
            }
            tabstr.value = items[state.currentPage]
        }
    }
}
