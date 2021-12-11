package com.jesen.compose_bili.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesen.compose_bili.ui.theme.gray300
import com.jesen.compose_bili.ui.theme.gray600
import com.jesen.compose_bili.ui.theme.gray700
import com.jesen.compose_bili.utils.pageErrorSrc
import kotlin.random.Random

/**
 * 底部加载更多失败处理
 * */
@Composable
fun ErrorMoreRetryItem(retry: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        TextButton(
            onClick = { retry() },
            modifier = Modifier
                .padding(20.dp)
                .width(80.dp)
                .height(30.dp),
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(3.dp),
            colors = ButtonDefaults.textButtonColors(backgroundColor = gray300),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp,
            ),
        ) {
            Text(text = "请重试", color = gray600)
        }
    }
}

@Composable
fun NoMoreDataFindItem(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        TextButton(
            onClick = { onClick() },
            modifier = Modifier
                .padding(20.dp)
                .width(80.dp)
                .height(30.dp),
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(3.dp),
            colors = ButtonDefaults.textButtonColors(backgroundColor = gray300),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp,
            ),
        ) {
            Text(text = "已经没有更多数据啦 ~~ Click to top", color = gray600)
        }
    }
}


/**
 * 页面加载失败处理
 * */
@Composable
fun ErrorContent(retry: () -> Unit) {
    val errorResIndex = Random.nextInt(3)
    Column(
        modifier = Modifier
            .wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.padding(top = 10.dp, start = 20.dp),
            painter = painterResource(id = pageErrorSrc[errorResIndex]),
            contentDescription = null
        )
        Text(text = "请求失败，请检查网络", modifier = Modifier.padding(8.dp))
        TextButton(
            onClick = { retry() },
            modifier = Modifier
                .padding(20.dp)
                .width(80.dp)
                .height(30.dp),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(5.dp),
            colors = ButtonDefaults.textButtonColors(backgroundColor = gray300),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp,
            )
            //colors = ButtonDefaults
        ) { Text(text = "重试", color = gray700) }
    }
}

/**
 * 底部加载更多正在加载中...
 * */
@Composable
fun LoadingItem() {
    Row(
        modifier = Modifier
            .height(34.dp)
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(24.dp),
            color = gray600,
            strokeWidth = 2.dp
        )
        Text(
            text = "加载中...",
            color = gray600,
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 20.dp),
            fontSize = 18.sp,
        )
    }
}