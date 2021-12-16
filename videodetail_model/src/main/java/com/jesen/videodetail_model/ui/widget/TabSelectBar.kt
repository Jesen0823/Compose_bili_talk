package com.jesen.videodetail_model.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.jesen.retrofit_lib.model.VideoDetailData
import com.jesen.videodetail_model.R
import com.jesen.videodetail_model.ui.theme.bili_50
import com.jesen.videodetail_model.ui.theme.gray400
import com.jesen.videodetail_model.ui.theme.gray50
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun TabSelectBar(
    pagerState: PagerState,
    scope: CoroutineScope,
    detailData: VideoDetailData,

    ) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        elevation = 8.dp,
        color = MaterialTheme.colors.background,
    ) {
        TabRow(
            modifier = Modifier
                .width(120.dp)
                .padding(start = 48.dp, end = 200.dp),
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = bili_50
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
                    Text(text = stringResource(R.string.introduce_title), fontSize = 16.sp)
                },
                selectedContentColor = bili_50,
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
                                contentColor = Color.White,
                                backgroundColor = bili_50
                            ) {
                                Text(text = "12")
                            }
                        }
                    ) {
                        Text(text = stringResource(R.string.comment_title), fontSize = 16.sp)
                    }
                },
                selectedContentColor = bili_50,
                unselectedContentColor = LocalContentColor.current
            )
        }

        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = 240.dp, top = 7.dp, bottom = 7.dp, end = 16.dp)
                .border(width = 1.dp, color = gray400, shape = RoundedCornerShape(18.dp))
                .background(color = gray50)
                .clickable { },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "点我发弹幕",
                color = gray400,
                style = TextStyle(fontSize = 16.sp)
            )
            Image(
                painter = painterResource(id = R.drawable.dan_1),
                modifier = Modifier
                    .size(48.dp),
                contentDescription = "danmu"
            )
        }
    }

}