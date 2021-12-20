import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesen.common_util_lib.R
import com.jesen.common_util_lib.utils.oLog
import kotlin.math.roundToInt

/**
 * 嵌套滑动顶部布局
 * */

@Composable
fun ScrollableAppBar(
    modifier: Modifier = Modifier,
    headerBgColor: Color = Color.White,
    header: @Composable (() -> Unit) = { DefaultHeader(toolbarOffsetHeightPx = toolbarOffsetHeightPx) },
    navigationIcon: @Composable (() -> Unit) = { DefaultNavigationLeftTop() }, //默认为返回图标
    headerTop: @Composable (() -> Unit) = { DefaultHeaderTop() }, // toolBar顶部定义
    background: Color = MaterialTheme.colors.primary,
    scrollableAppBarHeight: Dp,
    toolbarOffsetHeightPx: MutableState<Float>, //向上偏移量
    toolBarHeight: Dp = 56.dp, // 应用栏高度
    navigationIconSize: Dp = 50.dp,// 导航图标大小
    navigationParentHeight: Dp,
) {

    // 应用栏最大向上偏移量
    val maxOffsetHeightPx = with(LocalDensity.current) {
        scrollableAppBarHeight.roundToPx().toFloat() - toolBarHeight.roundToPx().toFloat()
    }
    oLog("nested, maxOffsetHeightPx: $maxOffsetHeightPx")
    // header 偏移量参考值
    val headerOffsetWidthReferenceValue =
        with(LocalDensity.current) { navigationIconSize.roundToPx().toFloat() }

    // 滑动比例，从100% -> 0%
    var slidePercent = (maxOffsetHeightPx + toolbarOffsetHeightPx.value) / maxOffsetHeightPx

    Box(modifier = Modifier
        .height(scrollableAppBarHeight)
        .offset {
            IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) //设置偏移量
        }
        .fillMaxWidth()
        .background(color = background)
    ) {
        // headerTop
        headerTop()
        // 自定义应用栏
        Row(
            modifier = modifier
                .offset {
                    IntOffset(
                        x = 0,
                        y = -toolbarOffsetHeightPx.value.roundToInt() //保证应用栏是始终不动的
                    )
                }
                .height(scrollableAppBarHeight)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 导航图标
            Box(
                modifier = Modifier
                    .size(navigationIconSize * slidePercent)
                    .align(Alignment.CenterVertically)
                    .padding(start = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                navigationIcon()
            }
        }

        Box(
            modifier = Modifier
                .height(toolBarHeight) //和ToolBar同高
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .offset {
                    IntOffset(
                        x = -((toolbarOffsetHeightPx.value / maxOffsetHeightPx) * headerOffsetWidthReferenceValue).roundToInt(),
                        y = 0
                    )
                }
                .background(color = headerBgColor),
            contentAlignment = Alignment.CenterStart
        ) {
            //header()
            DefaultHeader(toolbarOffsetHeightPx = toolbarOffsetHeightPx)
        }
    }
}

@Composable
fun DefaultNavigationLeftTop() {
    Icon(
        imageVector = Icons.Filled.ArrowBack,
        contentDescription = "ArrowBack",
        tint = Color.White
    )
}

@Composable
fun DefaultHeader(toolbarOffsetHeightPx: MutableState<Float>) {
    Text(
        text = "toolbar offset is px:${toolbarOffsetHeightPx.value},dp:}",
        color = Color.Blue,
        modifier = Modifier
            .padding(start = 20.dp)
            .layoutId("header_me"),
        fontSize = 20.sp
    )
}

@Composable
fun DefaultHeaderTop() {
    Image(
        painter = painterResource(id = R.drawable.bannerp),
        contentDescription = "background",
        contentScale = ContentScale.FillBounds
    )
}