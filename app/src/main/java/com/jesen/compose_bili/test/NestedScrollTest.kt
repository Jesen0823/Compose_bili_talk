package com.jesen.compose_bili.test

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun NestedScrollTest() {
    // 这里打算用内置嵌套滚动的 LazyColumn, 还打算给LazyColumn找一个爸爸，并参与爸爸的嵌套滚动
    // 那就给 LazyColumn找个折叠toolbar吧，叫他爸爸
    val toolbarHeight = 48.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    // 折叠toolbar的偏移量
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }

    // 给嵌套滚动加一个监听nestedScrollConnection，来监听滚动情况
    // 以下发生在儿子 LazyColumn
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // 如果需要，尝试在LazyColumn之前消费折叠工具栏，因此预滚动
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                // here's the catch这里有个陷阱: 假设折叠工具栏消费了0, 但是为了好的视觉体验想办法滚动LazyColumn
                // We're basically watching scroll without taking it 我们基本上看是不滚动的
                return Offset.Zero
            }
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            // 加到滚动系统的父类上面
            .nestedScroll(nestedScrollConnection)
    ) {
        // 列表将支持嵌套滚动，并将滚动情况通知我们
        // LazyColumn 高度和顶部高度都是固定的
        LazyColumn(contentPadding = PaddingValues(top = toolbarHeight)) {
            items(100) { index ->
                Text(
                    "I'm item $index", modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
        // TopAppBar的高度是固定的，但是顶部位置动态变化
        TopAppBar(
            modifier = Modifier
                .height(toolbarHeight)
                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
            title = { Text("toolbar offset is ${toolbarOffsetHeightPx.value}") }
        )
    }
}


@ExperimentalMaterialApi
@Composable
fun SwipeableSample() {
    val width = 96.dp
    val squareSize = 48.dp

    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1) // Maps anchor points (in px) to states

    Box(
        modifier = Modifier
            .width(width)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
            .background(Color.LightGray)
    ) {
        Box(
            Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .size(squareSize)
                .background(Color.DarkGray)
        )
    }
}