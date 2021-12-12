package com.jesen.bilibanner

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState


/**
 * 圆形指示器
 *
 * @param indicatorColor 指示器默认颜色
 * @param selectIndicatorColor 指示器选中颜色
 * @param indicatorDistance 指示器之间的距离
 * @param indicatorSize 指示器默认圆大小
 * @param selectIndicatorSize 指示器选中圆大小
 * @param gravity 指示器位置
 */
class PointIndicator(
    private var indicatorColor: Color = Color(30, 30, 33, 90),
    private var selectIndicatorColor: Color = Color.Green,
    private var indicatorDistance: Int = 50,
    private var indicatorSize: Float = 10f,
    private var selectIndicatorSize: Float = 13f,
) {

    @ExperimentalPagerApi
    @Composable
    fun DrawIndicator(pagerState: PagerState) {
        for (pageIndex in 0 until pagerState.pageCount) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val color: Color
                val inSize: Float
                if (pageIndex == pagerState.currentPage) {
                    color = selectIndicatorColor
                    inSize = selectIndicatorSize
                } else {
                    color = indicatorColor
                    inSize = indicatorSize
                }
                val start = canvasWidth - pagerState.pageCount * indicatorDistance - 100f
                drawCircle(
                    color,
                    inSize,
                    center = Offset(start + pageIndex * indicatorDistance, canvasHeight - 50f)
                )
            }
        }
    }
}
