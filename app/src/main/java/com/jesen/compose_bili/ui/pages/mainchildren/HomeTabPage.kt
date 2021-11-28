package com.jesen.compose_bili.ui.pages.mainchildren

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jesen.compose_bili.R
import kotlinx.coroutines.launch

/**
 * 首页 HorizontalPager 和 TabRow的使用
 * ScrollableTabRow: 可滑动的 TabView
 *
 * implement 'com.google.accompanist:accompanist-pager:0.18.0'
 *
 * */

@ExperimentalPagerApi
@Composable
fun HomeTabPage() {
    val items = listOf("推荐", "电影", "电视剧", "综艺", "纪录片", "娱乐", "新闻")
    Surface(color = MaterialTheme.colors.background) {

        val tabstr = remember {
            mutableStateOf(items[0])
        }

        val scope = rememberCoroutineScope()

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

        Column(modifier = Modifier.fillMaxSize()) {
            Column {

                // 固定长度的TabView
                TabRow(
                    selectedTabIndex = items.indexOf(tabstr.value),
                    modifier = Modifier.fillMaxWidth(),
                    indicator = { tabIndicator ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(
                                tabIndicator[items.indexOf(
                                    tabstr.value
                                )]
                            )
                        )
                    },
                    divider = {}
                ) {
                    items.forEachIndexed { index, title ->
                        val selected = index == items.indexOf(tabstr.value)
                        Tab(
                            modifier = Modifier.background(color = Color.Gray),
                            selected = selected,
                            onClick = {
                                tabstr.value = items[index]
                                scope.launch {
                                    state.scrollToPage(index)
                                }
                            },
                            text = { Text(text = title,color = Color.Green) },
                            selectedContentColor = Color.Blue,
                            unselectedContentColor = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 可滑动TabView
                ScrollableTabRow(
                    selectedTabIndex = items.indexOf(tabstr.value),
                    modifier = Modifier.wrapContentWidth(),
                    edgePadding = 16.dp,
                    indicator = { tabIndicator ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(
                                tabIndicator[items.indexOf(
                                    tabstr.value
                                )]
                            ),
                            color = Color.Cyan
                        )
                    },
                    backgroundColor = colorResource(id = R.color.purple_500),
                    divider = {
                        TabRowDefaults.Divider()
                    }
                ) {
                    items.forEachIndexed { index, title ->
                        val selected = index == items.indexOf(tabstr.value)
                        Tab(
                            modifier = Modifier.background(color = colorResource(id = R.color.purple_200)),
                            text = { Text(title, color = Color.White) },
                            selected = selected,
                            selectedContentColor = colorResource(id = R.color.purple_500),
                            onClick = {
                                //state.value = index
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