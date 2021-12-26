package com.jesen.compose_bili.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesen.compose_bili.ui.theme.Compose_bili_talkTheme
import com.jesen.qrzxing.compose.ScanDecoration

class NestedTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Compose_bili_talkTheme {
                /* NestedWrapCustomLayout(
                     columnTop = 200.dp,
                     navigationIconSize = 50.dp,
                     toolBarHeight = 56.dp,
                     scrollableAppBarHeight = 200.dp,
                     columnState = rememberLazyListState(),
                     scrollableAppBarBgColor = Color.LightGray,
                     toolBar = { MyToolBar() },
                     navigationIcon = { MyNavigationLeftTop() }, //默认为返回图标
                     headerTop = { MyHeaderTop() },
                     backSlideProgress = { progress ->

                     }
                 ) {
                     items(100) { index ->
                         Text(
                             "I'm item $index",
                             modifier = Modifier
                                 .fillMaxWidth()
                                 .padding(16.dp)
                         )
                     }
                 }*/
                //ZxingDrawTestPage()
                //HourglassTest()
                //AnimDemoPendulumTest()
                ScanDecoration(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Transparent)
                )
            }
        }
    }
}


@Composable
fun MyNavigationLeftTop() {
    Icon(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Yellow),
        imageVector = Icons.Filled.Search,
        contentDescription = "search",
        tint = Color.Red
    )
}

@Composable
fun MyToolBar() {
    Text(
        text = "toolbar toolbar toolbar toolbar",
        color = Color.Blue,
        modifier = Modifier
            .padding(start = 20.dp)
            .layoutId("header_me"),
        fontSize = 20.sp
    )
}

@Composable
fun MyHeaderTop() {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        painter = painterResource(id = com.jesen.common_util_lib.R.drawable.bannerp),
        contentDescription = "background",
        contentScale = ContentScale.FillBounds
    )
}