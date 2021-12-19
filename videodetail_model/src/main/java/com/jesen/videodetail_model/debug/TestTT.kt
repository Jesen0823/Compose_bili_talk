package com.jesen.videodetail_model.debug

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesen.videodetail_model.R
import com.jesen.videodetail_model.ui.theme.gray400
import com.jesen.videodetail_model.ui.theme.gray50

@Composable
fun TestDanMuButtn() {
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .padding(start = 240.dp, top = 7.dp, bottom = 7.dp, end = 16.dp)
            .border(width = 1.dp, color = gray400, shape = RoundedCornerShape(18.dp))
            .background(color = gray50)
            .clickable { },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .padding(start = 12.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colorStops = arrayOf(0.0f to Color.White, 1.0f to Color.Gray),
                    ),
                    shape = RoundedCornerShape(topStart = 18.dp, bottomStart = 18.dp)
                ),
            text = "点我发弹幕",
            color = gray400,
            style = TextStyle(fontSize = 16.sp)
        )
        Image(
            modifier = Modifier
                .size(48.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colorStops = arrayOf(0.0f to Color.White, 1.0f to Color.Gray),
                    ),
                    shape = RoundedCornerShape(topStart = 18.dp, bottomStart = 18.dp)
                ),
            painter = painterResource(id = R.drawable.dan_1),
            contentDescription = "danmu"
        )
    }
}