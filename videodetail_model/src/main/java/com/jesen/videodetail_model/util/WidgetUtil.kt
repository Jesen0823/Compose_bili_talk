package com.jesen.videodetail_model.util

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.OndemandVideo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.annotation.ExperimentalCoilApi
import com.jesen.common_util_lib.utils.CoilImage
import com.jesen.retrofit_lib.model.VideoM
import com.jesen.videodetail_model.ui.theme.bili_90
import com.jesen.videodetail_model.ui.theme.black87
import com.jesen.videodetail_model.ui.theme.gray200
import com.jesen.videodetail_model.ui.theme.gray600


@ExperimentalCoilApi
@ExperimentalMaterialApi
@Composable
fun SmallVideoCard(
    video: VideoM,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        color = Color.White,
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .padding(bottom = 6.dp),
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(start = 15.dp, end = 15.dp, bottom = 5.dp)
        ) {
            val (divideLine, coverImage, length, title, upTag, upName, viewNum, replyNum, moreIcon) = createRefs()
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(divideLine) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)

                    }, color = gray200, thickness = 1.dp
            )
            CoilImage(
                url = video.cover,
                modifier = Modifier
                    .height(100.dp)
                    .padding(bottom = 8.dp)
                    .aspectRatio(16 / 9f)
                    .constrainAs(coverImage) {
                        top.linkTo(divideLine.bottom, margin = 8.dp)
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
                        top.linkTo(divideLine.bottom, margin = 12.dp)
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
            text = com.jesen.common_util_lib.utils.countFormat(count),
            modifier = Modifier.padding(start = 6.dp),
            style = TextStyle(fontSize = 12.sp, color = color)
        )
    }
}