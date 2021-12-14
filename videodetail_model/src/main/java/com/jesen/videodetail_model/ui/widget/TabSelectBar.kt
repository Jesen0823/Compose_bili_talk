package com.jesen.videodetail_model.ui.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.jesen.retrofit_lib.model.VideoDetailData
import com.jesen.videodetail_model.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun TabSelectBar(
    pagerState: PagerState,
    scope: CoroutineScope,
    detailData: VideoDetailData,

    ) {
    TabRow(
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                color = MaterialTheme.colors.primary
            )
        },
        backgroundColor = MaterialTheme.colors.background,
    ) {
        Tab(
            modifier = Modifier.height(45.dp),
            selected = pagerState.currentPage == 0,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(0)
                }
            },
            text = {
                Text(text = stringResource(R.string.introduce_title))
            },
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = LocalContentColor.current
        )
        Tab(
            modifier = Modifier.height(45.dp),
            selected = pagerState.currentPage == 1,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(1)
                }
            },
            text = {
                BadgedBox(
                    badge = {
                        Badge(
                            backgroundColor = MaterialTheme.colors.primary
                        ) {
                            Text(
                                text = "12"
                            )
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.comment_title))
                }
            },
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = LocalContentColor.current
        )
    }
}