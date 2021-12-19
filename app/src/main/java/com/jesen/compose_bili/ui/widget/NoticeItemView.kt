package com.jesen.compose_bili.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesen.compose_bili.ui.theme.gray200
import com.jesen.compose_bili.ui.theme.gray400
import com.jesen.compose_bili.ui.theme.gray600
import com.jesen.compose_bili.ui.theme.gray800
import com.jesen.retrofit_lib.model.Notice

@Composable
fun NoticeItemView(notice: Notice, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .wrapContentHeight()
            .clickable { onClick() },
    ) {
        Spacer(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(color = gray200)
        )
        Icon(
            modifier = Modifier
                .size(32.dp)
                .padding(4.dp)
                .align(Alignment.CenterStart),
            imageVector = Icons.Rounded.Notifications,
            tint = gray400,
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 34.dp, top = 6.dp, bottom = 18.dp),
            text = notice.title,
            fontSize = 18.sp,
            textAlign = TextAlign.Start,
            color = gray800,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 34.dp, top = 24.dp),
            text = notice.subtitle,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            color = gray600,
            fontStyle = FontStyle.Italic
        )
    }
}