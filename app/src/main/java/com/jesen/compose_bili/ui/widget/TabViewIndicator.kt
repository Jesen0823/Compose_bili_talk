package com.jesen.compose_bili.ui.widget

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jesen.compose_bili.ui.theme.bili_50

/**
 *  自定义TabView指示器
 */

@Composable
fun BiliIndicator(color: Color = bili_50, modifier: Modifier = Modifier) {
    // 指示器的外形具体绘制
    Box(
        modifier
            .padding(5.dp)
            .fillMaxSize()
            //.border(BorderStroke(2.dp, color), RoundedCornerShape(5.dp))
            .drawWithContent(onDraw = {
                drawLine(
                    color = color,
                    strokeWidth = 8f,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    pathEffect = PathEffect.cornerPathEffect(radius = 5f)
                )
            })
    )
}

@Composable
fun BiliIndicator2(
    height: Dp = TabRowDefaults.IndicatorHeight,
    color: Color = bili_50,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .padding(top = 5.dp, bottom = 2.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .height(height)
            .background(color = color, shape = RoundedCornerShape(size = 4.dp))
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BiliAnimatedIndicator(tabPositions: List<TabPosition>, selectedTabIndex: Int) {
    val colors = listOf(Color.Yellow, Color.Red, Color.Green)
    val transition = updateTransition(selectedTabIndex, label = "Transition")
    val indicatorStart by transition.animateDp(
        label = "Indicator Start",
        transitionSpec = {
            // 如果向右移动，则右边移动速度快
            // 如果向左移动，则左边速度快
            // 阻尼比dampingRatio默认1f，
            // 刚度：stiffness 刚度对立面是柔性，刚度越大弹簧伸缩速度越快，这里设小点
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 50f)
            } else {
                spring(dampingRatio = 1f, stiffness = 1000f)
            }
        }
    ) {
        tabPositions[it].left
    }

    val indicatorEnd by transition.animateDp(
        label = "Indicator End",
        transitionSpec = {
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 1000f)
            } else {
                spring(dampingRatio = 1f, stiffness = 50f)
            }
        }
    ) {
        tabPositions[it].right
    }

    // 可选颜色 列表colors指定的其中一个
    val indicatorColor1 by transition.animateColor(label = "Indicator Color") {
        colors[it % colors.size]
    }

    //val indicatorColor2 = ColorUtil.getRandomColor(bili_50)

    BiliIndicator2(
        // indicator当前颜色，指定颜色有默认值
        //color = indicatorColor1,
        height = 5.dp,
        modifier = Modifier
            // 填满整个TabView,并把指示器放在开始位置
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
            // 设置偏移量定位指示器的开始位置
            .offset(x = indicatorStart)
            // 在选项卡之间移动时，指示器的宽度与动画宽度一致
            .width(indicatorEnd - indicatorStart)
    )
}

/**
 * 指示器测试
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TestIndicatorOfTabView() {
    var state by remember { mutableStateOf(0) }
    val titles = listOf(
        "TAB 1",
        "TAB 2",
        "TAB 3 WITH LOTS OF TEXT",
        "TAB 4",
        "TAB 5",
        "TAB 6 WITH LOTS OF TEXT",
        "TAB 7",
        "TAB 8",
        "TAB 9 WITH LOTS OF TEXT",
        "TAB 10"
    )
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        BiliAnimatedIndicator(
            tabPositions = tabPositions,
            selectedTabIndex = state
        )
    }

    Column {
        ScrollableTabRow(
            selectedTabIndex = state,
            indicator = indicator,
            backgroundColor = Color.White
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = state == index,
                    onClick = { state = index }
                )
            }
        }
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Scrolling fancy transition tab ${state + 1} selected",
            style = MaterialTheme.typography.body1
        )
    }
}