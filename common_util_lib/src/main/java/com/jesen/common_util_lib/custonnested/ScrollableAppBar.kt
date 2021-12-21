import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
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
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

/**
 * 嵌套滑动顶部布局
 * */

@Composable
fun ScrollableAppBar(
    slideProgress: Float,
    headerBgColor: Color = Color.Transparent,
    toolBar: @Composable () -> Unit = { DefaultToolBar(slideProgress = slideProgress) },
    navigationIcon: @Composable () -> Unit = { DefaultNavigationLeftTop() }, //默认为返回图标
    headerTop: @Composable () -> Unit = { DefaultHeaderTop() }, // toolBar顶部定义
    background: Color = MaterialTheme.colors.primary,
    scrollableAppBarHeight: Dp,
    toolBarHeight: Dp = 56.dp, // 应用栏高度
    navigationIconSize: Dp = 50.dp,// 导航图标大小
    extendUsrInfo: @Composable() (() -> Unit)? = null,
) {

    // 主要尺寸转化为px
    val toolBarHeightPx =
        with(LocalDensity.current) { toolBarHeight.roundToPx().toFloat() }
    val scrollableAppBarHeightPx =
        with(LocalDensity.current) { scrollableAppBarHeight.roundToPx().toFloat() }
    // 绝对偏移进度 0.1-> 0.0
    val abstractOffset = 1 - slideProgress.absoluteValue

    // 应用栏最大向上偏移量
    val maxOffsetHeightPx = scrollableAppBarHeightPx - toolBarHeightPx

    oLog("nested, maxOffsetHeightPx: $maxOffsetHeightPx,slideProgress:$slideProgress,abstractOffset:$abstractOffset")

    /** 父子控件及偏移 */
    Box(
        modifier = Modifier
            .height(scrollableAppBarHeight)
            //设置偏移量
            .offset {
                //IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt())
                IntOffset(
                    x = 0,
                    y = maxOffsetHeightPx
                        .times(slideProgress)
                        .toInt()
                )
            }
            .fillMaxWidth()
            //.border(width = 2.dp, color = Color.Red) // 用于调试
            .background(color = background)
    ) {

        // 1. 顶部背景
        headerTop()

        // 2. 导航图标
        val navIconTop = (scrollableAppBarHeight.minus(toolBarHeight).minus(navigationIconSize)) / 2
        val navIconStart = 50.dp
        val navIconStartPx =
            with(LocalDensity.current) { navIconStart.roundToPx().toFloat() }
        val navOffsetY = (-slideProgress * (maxOffsetHeightPx + toolBarHeightPx) / 2).toInt()

        // 设定图标缩放范围
        val navScale = abstractOffset.coerceIn(0.4f, 1.0f)
        Box(
            modifier = Modifier
                .padding(
                    top = navIconTop,
                    start = navIconStart.times(abstractOffset.coerceIn(0.32f, 1.0f))
                )
                .offset {
                    IntOffset(
                        x = (slideProgress * (navIconStartPx - toolBarHeightPx / 2)).toInt(),
                        y = navOffsetY
                    )
                }
                .size(navigationIconSize)
                .scale(navScale),
            contentAlignment = Alignment.Center
        ) {
            navigationIcon()
        }

        // 3. 自定义toolBar
        Box(
            modifier = Modifier
                .height(toolBarHeight) //和ToolBar同高
                .fillMaxWidth()
                // toolBar定位在大布局底部，所以Y方向不需要偏移，只跟着大背景走
                .align(Alignment.BottomStart)
                // X轴方向需要向右偏移自身高度，即左边留出 h*h 区域
                .offset {
                    IntOffset(
                        x = -(slideProgress * toolBarHeightPx).roundToInt(),
                        y = 0
                    )
                }
                .background(color = headerBgColor),
            contentAlignment = Alignment.CenterStart
        ) {
            toolBar()
        }

        // 可选布局
        if (extendUsrInfo != null) {
            val exStart = navIconStart.plus(navigationIconSize).plus(32.dp)
            Box(
                modifier = Modifier
                    .height(scrollableAppBarHeight.minus(toolBarHeight))
                    .align(Alignment.TopStart)
                    .padding(end = 16.dp, start = exStart)
                //.border(width = 2.dp, color = Color.White) // 用于调试
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        // toolBar定位在大布局底部，所以Y方向不需要偏移，只跟着大背景走
                        .align(Alignment.CenterStart)
                        //.border(width = 1.dp, color = Color.Red) // 用于调试
                        .offset {
                            IntOffset(
                                x = 0,
                                y = (-(maxOffsetHeightPx.div(2))
                                    .roundToInt()
                                    .times(slideProgress)).toInt()
                            )
                        }
                        .scale((1 + slideProgress.absoluteValue).coerceIn(1.0f, 1.5f))
                        .alpha(abstractOffset.coerceIn(0.0f, 1.0f))
                    // X轴方向需要向右偏移自身高度，即左边留出 h*h 区域
                ) {
                    extendUsrInfo()
                }
            }
        }
    }
}


/***************************************以下 Test 默认控件 *************************************/

@Composable
fun DefaultNavigationLeftTop() {
    Icon(
        imageVector = Icons.Filled.ArrowBack,
        contentDescription = "ArrowBack",
        tint = Color.White
    )
}

@Composable
fun DefaultToolBar(slideProgress: Float) {
    Text(
        text = "toolbar offset is px:${slideProgress},dp:}",
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