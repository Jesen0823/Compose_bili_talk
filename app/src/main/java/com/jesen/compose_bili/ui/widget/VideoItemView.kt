package com.jesen.compose_bili.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.OndemandVideo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.jesen.compose_bili.model.VideoM
import com.jesen.compose_bili.ui.theme.black87
import com.jesen.compose_bili.ui.theme.gray600
import com.jesen.compose_bili.utils.CoilCircleImage
import com.jesen.compose_bili.utils.CoilImage
import com.jesen.compose_bili.utils.countFormat
import com.jesen.compose_bili.utils.durationTransform

/**
 * 视频Item卡片
 * */
@ExperimentalMaterialApi
@ExperimentalCoilApi
@Composable
fun VideoItemCard(
    video: VideoM,
    onClick: () -> Unit,
    index: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(224.dp)
            .padding(
                top = 4.dp,
                bottom = 4.dp,
                start = if ((index and 1) == 0) 0.dp else 3.dp,
                end = if ((index and 1) == 0) 3.dp else 0.dp,
            ),
        shape = RoundedCornerShape(6.dp),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.surface,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            CoilImage(
                url = video.cover,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f),
                topLeft = 12f, topRight = 12f
            )
            // 互动小图标
            VideoInfo(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 90.dp),
                video
            )
            Text(
                modifier = Modifier.padding(top = 128.dp, start = 8.dp),
                text = video.title,
                style = TextStyle(
                    color = black87,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // 作者信息
            AuthorInfo(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd)
                    .height(40.dp),
                //.align(alignment = Alignment.Vertical.)
                //.padding(bottom = 8.dp),
                authImg = video.owner.face,
                authName = video.owner.name
            )
        }
    }
}

@Composable
fun VideoInfo(modifier: Modifier, video: VideoM) {
    Row(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    startY = 0f,
                    //endY = 0f,
                    colors = listOf(Color.Transparent, Color.Black)
                ),
                alpha = 0.9f
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconTextSmall(Icons.Rounded.OndemandVideo, video.view, Modifier.padding(start = 8.dp))
        IconTextSmall(Icons.Rounded.FavoriteBorder, video.like, Modifier.padding(start = 24.dp))
        Spacer(modifier = Modifier.width(38.dp))
        Text(
            text = durationTransform(video.duration),
            style = TextStyle(color = Color.White, fontSize = 12.sp)
        )
    }
}


@Composable
fun IconTextSmall(
    imageVector: ImageVector,
    count: Int,
    modifier: Modifier? = null,
    color: Color = Color.White
) {
    Row(
        modifier = modifier ?: Modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = color
        )
        Text(
            text = countFormat(count),
            modifier = Modifier.padding(start = 6.dp),
            style = TextStyle(fontSize = 12.sp, color = color)
        )
    }
}

@ExperimentalCoilApi
@Composable
fun AuthorInfo(modifier: Modifier, authImg: String, authName: String) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoilCircleImage(
            url = authImg, modifier = Modifier
                .size(36.dp)
                .padding(start = 8.dp)
        )
        Text(
            modifier = Modifier
                .padding(start = 10.dp)
                .width(60.dp),
            text = authName,
            style = TextStyle(fontSize = 12.sp, color = black87),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(48.dp))
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = null,
            modifier = Modifier
                .size(38.dp)
                .padding(start = 20.dp),
            tint = gray600
        )
    }
}
