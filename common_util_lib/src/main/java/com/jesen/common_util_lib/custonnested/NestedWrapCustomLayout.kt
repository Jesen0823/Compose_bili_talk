package com.jesen.compose_bili.test

import ScrollableAppBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 自定义嵌套滑动布局
 */
@Composable
fun NestedWrapCustomLayout(
    columnTop: Dp = 200.dp,
    scrollableAppBarHeight: Dp = columnTop,
    toolBarHeight: Dp = 56.dp,
    navigationIconSize: Dp = 50.dp,
    navigationIcon: @Composable (() -> Unit),
    scrollableAppBarBgColor: Color,
    headerTop: @Composable (() -> Unit),
    toolBar: @Composable (() -> Unit),
    backSlideProgress: (Float) -> Unit,
    columnState: LazyListState = rememberLazyListState(),
    listContent: LazyListScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Gray
    ) {
        // ToolBar 最大向上位移量56
        val maxUpPx = with(LocalDensity.current) {
            columnTop.roundToPx().toFloat() - toolBarHeight.roundToPx().toFloat()
        }
        // ToolBar 最小向上位移量
        val minUpPx = 0f
        // 偏移折叠工具栏上移高度
        val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
        // 现在，让我们创建与嵌套滚动系统的连接并聆听子 LazyColumn 中发生的滚动
        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    val newOffset = toolbarOffsetHeightPx.value + delta
                    toolbarOffsetHeightPx.value = newOffset.coerceIn(-maxUpPx, minUpPx)
                    return Offset.Zero
                }
            }
        }
        Box(
            Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)
        ) {

            LazyColumn(
                contentPadding = PaddingValues(top = columnTop),
                state = columnState,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),

                ) {
                listContent()
            }

            ScrollableAppBar(
                scrollableAppBarHeight = scrollableAppBarHeight,
                toolbarOffsetHeightPx = toolbarOffsetHeightPx,
                toolBarHeight = toolBarHeight,
                navigationIcon = navigationIcon,
                headerTop = headerTop,
                toolBar = toolBar,
                navigationIconSize = navigationIconSize,
                background = scrollableAppBarBgColor,
                backSlideProgress = backSlideProgress
            )
        }
    }
}