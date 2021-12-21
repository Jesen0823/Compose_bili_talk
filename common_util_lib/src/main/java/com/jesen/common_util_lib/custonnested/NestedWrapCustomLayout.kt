package com.jesen.common_util_lib.custonnested

import ScrollableAppBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
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
import com.jesen.common_util_lib.utils.oLog
import kotlin.math.absoluteValue


/**
 * 自定义嵌套滑动布局
 * @param columnTop 滑动列表到顶部距离
 * @param scrollableAppBarHeight 整个顶部的高度，可以与[columnTop]相等
 * @param toolBarHeight 自定义toolbar的高度
 * @param navigationIconSize 左上角图标的大小，主要是方便偏移计算
 * @param navigationIcon 左上角图标，如头像
 * @param scrollableAppBarBgColor 整个顶部的背景色
 * @param headerTop 顶部图片或颜色布局
 * @param toolBar 自定义toolbar
 * @param extendUsrInfo 左上角图标靠右的布局内容，如用户名
 * @param backSlideProgress 滑动进度监听回调
 * @param columnState LazyColumn的状态参数
 * @param listContent LazyColumn的内容 @[不能是嵌套 LazyColumn]
 *
 */
@Composable
fun NestedWrapCustomLayout(
    columnTop: Dp = 200.dp,
    scrollableAppBarHeight: Dp = 200.dp,
    toolBarHeight: Dp = 56.dp,
    navigationIconSize: Dp = 50.dp,
    navigationIcon: @Composable (() -> Unit),
    scrollableAppBarBgColor: Color,
    headerTop: @Composable (() -> Unit),
    toolBar: @Composable (() -> Unit),
    extendUsrInfo: @Composable (() -> Unit)? = null,
    backSlideProgress: (Float) -> Unit,
    columnState: LazyListState = rememberLazyListState(),
    listContent: LazyListScope.() -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = toolBarHeight.plus(8.dp)),
        color = MaterialTheme.colors.surface
    ) {
        //  最大向上位移量56
        val maxUpPx = with(LocalDensity.current) {
            columnTop.roundToPx().toFloat() - toolBarHeight.roundToPx().toFloat()
        }

        // ToolBar 最小向上位移量
        val minUpPx = 0f

        // 偏移折叠工具栏上移高度
        val slideOffsetHeightPx = remember { mutableStateOf(0f) }
        val slideProgress = slideOffsetHeightPx.value.div(maxUpPx)

        val abstractOffset = 1 - slideProgress.absoluteValue
        // 回调进度给外部，可选
        backSlideProgress(abstractOffset)

        // 创建与嵌套滚动系统的连接nestedScrollConnection,监听LazyColumn 中的滚动
        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    oLog("scroll: delta:$delta")
                    // 累加LazyColumn滑动距离
                    val newOffset = slideOffsetHeightPx.value + delta
                    slideOffsetHeightPx.value = newOffset.coerceIn(-maxUpPx, minUpPx)
                    return Offset.Zero
                }
            }
        }
        // 父滑动列表
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
                // 滑动列表的内容
                listContent()
            }

            ScrollableAppBar(
                slideProgress = slideProgress,
                scrollableAppBarHeight = scrollableAppBarHeight,
                toolBarHeight = toolBarHeight,
                navigationIcon = navigationIcon,
                headerTop = headerTop,
                toolBar = toolBar,
                navigationIconSize = navigationIconSize,
                background = scrollableAppBarBgColor,
                extendUsrInfo = extendUsrInfo
            )
        }
    }
}