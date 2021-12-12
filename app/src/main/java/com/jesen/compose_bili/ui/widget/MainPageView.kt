package com.jesen.compose_bili.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.bilibanner.BannerPager
import com.jesen.bilibanner.bean.BannerData
import com.jesen.compose_bili.ui.theme.gray100
import com.jesen.compose_bili.ui.theme.gray600
import com.jesen.compose_bili.utils.CoilCircleImage
import com.jesen.compose_bili.utils.oLog


/**
 * 首页顶部 AppBar
 */
@ExperimentalCoilApi
@Composable
fun MainTopBarUI(
    searchClick: () -> Unit,
    headerClick: () -> Unit,
    actionsClick: () -> Unit
) {
    val testUrl =
        "http://img.wmtp.net/wp-content/uploads/2020/09/99e56d8ab85447e79a92e1ae4d25a051!400x400.jpeg"
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 10.dp, horizontal = 23.dp)
                    .background(color = gray100, shape = RoundedCornerShape(24.dp))
                    .clickable { searchClick() },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    modifier = Modifier
                        .size(34.dp)
                        .padding(start = 8.dp, end = 5.dp),
                    contentDescription = "search"
                )
                Text(text = "热搜 | 黄河...", style = TextStyle(fontSize = 16.sp))
            }
        },
        navigationIcon = {
            IconButton(modifier = Modifier.fillMaxHeight(), onClick = headerClick) {
                CoilCircleImage(
                    testUrl,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(8.dp)
                )
            }
        },
        actions = {
            IconButton(modifier = Modifier.fillMaxHeight(), onClick = actionsClick) {
                Icon(
                    imageVector = Icons.Rounded.Email,
                    modifier = Modifier
                        .size(36.dp)
                        .padding(end = 8.dp),
                    contentDescription = "notice"
                )
            }
        },
        backgroundColor = Color.White,
        contentColor = gray600,
        elevation = 10.dp
    )
}

/**
 * 默认Banner封装
 * */
@ExperimentalPagerApi
@Composable
fun BiliBanner(
    modifier: Modifier = Modifier,
    items: MutableList<BannerData>,
    itemOnClick: (BannerData) -> Unit
) {
    BannerPager(
        modifier = modifier.padding(top = 10.dp),
        items = items,
    ) { data ->
        oLog(" banner, click it，id:${data.id}, url:${data.url}")
        itemOnClick(data)
    }
}