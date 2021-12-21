package com.jesen.videodetail_model.util

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.OndemandVideo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.annotation.ExperimentalCoilApi
import com.jesen.common_util_lib.utils.CoilImage
import com.jesen.common_util_lib.utils.countFormat
import com.jesen.retrofit_lib.model.VideoM
import com.jesen.videodetail_model.ui.theme.bili_90
import com.jesen.videodetail_model.ui.theme.black87
import com.jesen.videodetail_model.ui.theme.gray600

/**
 * 视频小卡片
 * */
@ExperimentalCoilApi
@ExperimentalMaterialApi
@Composable
fun SmallVideoCard(
    video: VideoM,
    onClick: (VideoM) -> Unit,
) {
    Surface(
        onClick = { onClick(video) },
        color = Color.White,
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .padding(bottom = 6.dp),
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(start = 15.dp, end = 15.dp, bottom = 5.dp)
        ) {
            val (coverImage, length, title, upTag, upName, viewNum, replyNum, moreIcon) = createRefs()
            CoilImage(
                url = video.cover,
                modifier = Modifier
                    .height(100.dp)
                    .padding(bottom = 8.dp)
                    .aspectRatio(16 / 9f)
                    .constrainAs(coverImage) {
                        top.linkTo(parent.top, margin = 8.dp)
                        start.linkTo(parent.start)
                    },
                topRight = 8f,
                bottomLeft = 8f
            )

            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .background(gray600.copy(alpha = 0.3f), shape = RoundedCornerShape(2.dp))
                    .padding(horizontal = 4.dp)
                    .constrainAs(length) {
                        bottom.linkTo(coverImage.bottom, margin = 10.dp)
                        end.linkTo(coverImage.end, margin = 6.dp)
                    },
                text = com.jesen.common_util_lib.utils.durationTransform(video.duration),
                style = TextStyle(fontSize = 12.sp, color = Color.White)
            )

            Text(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 12.dp)
                        start.linkTo(coverImage.end, margin = 8.dp)
                        end.linkTo(parent.end, margin = 6.dp)
                        width = Dimension.preferredWrapContent
                    },
                text = video.title,
                style = TextStyle(fontSize = 14.sp, color = black87),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .border(border = BorderStroke(width = 1.dp, color = bili_90))
                    .padding(horizontal = 2.dp)
                    .constrainAs(upTag) {
                        bottom.linkTo(viewNum.top, margin = 8.dp)
                        start.linkTo(coverImage.end, margin = 8.dp)
                        baseline.linkTo(upName.baseline)
                    },
                text = "UP",
                color = bili_90,
                style = TextStyle(
                    fontSize = 10.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                ),
                textDecoration = TextDecoration.None
            )


            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(upName) {
                        bottom.linkTo(viewNum.top, margin = 8.dp)
                        start.linkTo(upTag.end, margin = 8.dp)
                    },
                text = video.owner.name,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.Gray,
                ),
            )

            IconTextSmall(
                imageVector = Icons.Rounded.OndemandVideo,
                count = video.view,
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(viewNum) {
                        start.linkTo(coverImage.end, margin = 8.dp)
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                    },
                color = Color.Gray
            )

            IconTextSmall(
                imageVector = Icons.Rounded.Chat,
                count = video.reply,
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(replyNum) {
                        bottom.linkTo(parent.bottom, margin = 12.dp)
                        start.linkTo(viewNum.end, margin = 16.dp)
                        baseline.linkTo(viewNum.baseline)
                    },
                color = Color.Gray
            )

            Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier
                    .size(16.dp)
                    .constrainAs(moreIcon) {
                        end.linkTo(parent.end, margin = 6.dp)
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                    }
            )
        }
    }
}


/**
 * 左Icon右文字
 * */
@Composable
fun IconTextSmall(
    imageVector: ImageVector,
    count: Int = 0,
    modifier: Modifier,
    color: Color = Color.White,
    fontSize: TextUnit = 12.sp,
    iconSize: Dp = 12.dp
) {
    Row(
        modifier = modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier.size(iconSize),
            tint = color
        )
        Text(
            text = countFormat(count),
            modifier = Modifier.padding(start = 6.dp),
            style = TextStyle(fontSize = fontSize, color = color)
        )
    }
}


/**
 * 上Icon下文字
 * */
@Composable
fun VerticalIconText(
    imageVector: ImageVector,
    count: Int = 0,
    modifier: Modifier,
    color: Color = Color.Gray
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = color
        )
        Text(
            text = countFormat(count),
            modifier = Modifier.padding(top = 6.dp),
            style = TextStyle(fontSize = 14.sp, color = color)
        )
    }
}

/**
 * 图文按钮
 * */
@Composable
fun IconTextButton(
    modifier: Modifier,
    text: String,
    icon: ImageVector = Icons.Filled.Add,
    iconSecond: ImageVector = Icons.Filled.Done,
    contentColor: Color = Color.White,
    backgroundColor: Color = Color.Gray,
    contentDescription: String = "null",
    onClick: () -> Unit,
    eventState: MutableState<Boolean>

) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val pressState = interactionSource.collectIsPressedAsState()
    val pressText = if (pressState.value) "处理中..." else text
    Button(
        modifier = modifier,
        onClick = onClick,
        interactionSource = interactionSource,
        elevation = ButtonDefaults.elevation(defaultElevation = 4.dp, pressedElevation = 2.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 5.dp)

    ) {

        Icon(
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.CenterVertically),
            imageVector = if (eventState.value) iconSecond else icon,
            contentDescription = contentDescription,
            tint = contentColor
        )
        Text(
            modifier = Modifier
                .padding(start = 4.dp)
                .align(Alignment.CenterVertically),
            text = pressText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}