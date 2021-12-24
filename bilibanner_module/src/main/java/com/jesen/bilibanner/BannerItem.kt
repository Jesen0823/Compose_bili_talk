package com.jesen.bilibanner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.jesen.bilibanner.bean.BannerData
import com.jesen.common_util_lib.utils.CoilImage

/**
 * Banner 图片展示卡片
 *
 * @param bean banner Model
 * @param modifier
 * @param shape 图片圆角
 * @param contentScale 纵横比缩放
 * @param onBannerClick Banner 图片点击事件
 */
@Composable
fun BannerItem(
    data: BannerData,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(10.dp),
    contentScale: ContentScale,
    allCorner:Float=12f,
    onItemClick: (BannerData) -> Unit,
) {
    Card(
        shape = shape,
        modifier = modifier
    ) {
        CoilImage(
            contentScale=contentScale,
            allCorner=allCorner,
            url = data.imgUrl,
            modifier = Modifier.clickable(onClick = { onItemClick(data) }
            )
        )
    }
}
