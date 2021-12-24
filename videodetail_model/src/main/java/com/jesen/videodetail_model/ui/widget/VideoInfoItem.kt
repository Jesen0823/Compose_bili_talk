package com.jesen.videodetail_model.ui.widget

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.Wysiwyg
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesen.common_util_lib.utils.CoilCircleImage
import com.jesen.common_util_lib.utils.countFormat
import com.jesen.common_util_lib.utils.showToast
import com.jesen.retrofit_lib.model.VideoDetailData
import com.jesen.videodetail_model.ui.theme.bili_50
import com.jesen.videodetail_model.util.IconTextButton
import com.jesen.videodetail_model.util.IconTextSmall
import com.jesen.videodetail_model.util.VerticalIconText
import com.jesen.videodetail_model.viewmodel.DetailViewModel

@ExperimentalComposeUiApi
@Composable
fun VideoInfoItem(viewModel: DetailViewModel, detailData: VideoDetailData) {
    val videoInfo = detailData.videoInfo
    // 视频简介
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        // 展开按钮
        var isExpand by remember {
            mutableStateOf(false)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 头像
            CoilCircleImage(
                url = videoInfo.owner.face,
                modifier = Modifier
                    .size(72.dp)
                    .padding(16.dp),
            )
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .width(200.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // 用户名
                Text(
                    text = videoInfo.owner.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = bili_50
                )

                Text(
                    modifier = Modifier
                        .padding(top = 5.dp),
                    text = "${countFormat(videoInfo.owner.fans)}粉丝",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.width(42.dp))
            // 关注按钮
            val isFollowState = remember { mutableStateOf(false) }
            val context = LocalContext.current
            IconTextButton(
                modifier = Modifier
                    .height(32.dp)
                    .padding(end = 16.dp)
                    .wrapContentWidth(),
                text = if (isFollowState.value) "已关注" else "关注",
                backgroundColor = bili_50,
                eventState = isFollowState,
                onClick = {
                    viewModel.requestFollow(isFollow = isFollowState.value) { response ->
                        response?.let {
                            showToast(
                                context = context,
                                msg = if (it.isSuccess()) {
                                    if (it.read().data == true) "关注成功" else "已取消关注"
                                } else {
                                    "操作失败"
                                }
                            )
                            isFollowState.value = it.read().data ?: false
                        }
                    }
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, bottom = 5.dp, top = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = videoInfo.title,
                fontSize = 19.sp,
                color = MaterialTheme.colors.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            IconButton(
                modifier = Modifier
                    .size(48.dp)
                    .padding(5.dp),
                onClick = { isExpand = !isExpand }) {
                Icon(
                    imageVector = if (isExpand) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "expand"
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.wrapContentWidth()
        ) {

            IconTextSmall(
                modifier = Modifier.padding(start = 16.dp),
                imageVector = Icons.Filled.OndemandVideo,
                count = videoInfo.view,
                color = Color.Gray,
                fontSize = 14.sp,
                iconSize = 14.dp
            )

            IconTextSmall(
                modifier = Modifier.padding(start = 8.dp),
                imageVector = Icons.Filled.Wysiwyg,
                count = videoInfo.reply,
                color = Color.Gray,
                fontSize = 14.sp,
                iconSize = 14.dp
            )

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = videoInfo.createTime,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        AnimatedVisibility(
            visible = isExpand,
            enter = fadeIn() + expandVertically(
                animationSpec = tween(150)
            ),
            exit = fadeOut() + shrinkVertically(
                animationSpec = tween(150)
            )
        ) {
            SelectionContainer(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.medium,
                    LocalTextStyle provides LocalTextStyle.current.copy(
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 13.sp
                    )
                ) {
                    Text(
                        text = videoInfo.desc,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        /** 底部互动按钮 */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 点赞按钮
            val likeState = remember { mutableStateOf(detailData.isLike) }
            val unLikeState = remember { mutableStateOf(!detailData.isLike) }
            val unLikeNum = remember { mutableStateOf(0) }

            val favoriteState = remember { mutableStateOf(detailData.isFavorite) }

            val context = LocalContext.current
            VerticalIconText(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(5.dp)
                    .clickable(enabled = !likeState.value) {
                        viewModel.requestLike(isLike = true, videoId = videoInfo.vid) { response ->
                            response?.let {
                                showToast(
                                    context = context,
                                    msg = if (it.isSuccess()) {
                                        it.read().msg
                                    } else {
                                        it.errorMessage()
                                    }
                                )
                                likeState.value = it.read().data ?: false
                                if (it.isSuccess() && unLikeNum.value == 1) {
                                    unLikeNum.value -= 1
                                    unLikeState.value = false
                                }
                            }
                        }
                    },
                imageVector = Icons.Rounded.ThumbUp,
                count = videoInfo.like,
                color = if (likeState.value) bili_50 else Color.Gray
            )

            VerticalIconText(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(5.dp)
                    .clickable(enabled = !unLikeState.value) {
                        viewModel.requestLike(isLike = false, videoId = videoInfo.vid) { response ->
                            response?.let {
                                showToast(
                                    context = context,
                                    msg = if (it.isSuccess()) {
                                        it.read().msg
                                    } else {
                                        it.errorMessage()
                                    }
                                )
                                unLikeState.value = it.read().data ?: false
                                if (it.isSuccess() && unLikeNum.value == 0) {
                                    unLikeNum.value += 1
                                    likeState.value = false
                                }
                            }
                        }
                    },
                imageVector = Icons.Rounded.ThumbDown,
                count = unLikeNum.value,
                color = if (unLikeState.value) bili_50 else Color.Gray
            )

            VerticalIconText(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(5.dp)
                    .clickable {},
                imageVector = Icons.Rounded.MonetizationOn,
                count = videoInfo.coin
            )

            VerticalIconText(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(5.dp)
                    .clickable {
                        viewModel.requestFavorite(
                            isFavorite = !favoriteState.value,
                            videoId = videoInfo.vid
                        ) { response ->
                            response?.let {
                                showToast(
                                    context = context,
                                    msg = if (it.isSuccess()) {
                                        it.read().msg
                                    } else {
                                        it.errorMessage()
                                    }
                                )

                                if (it.isSuccess()) {
                                    favoriteState.value = !favoriteState.value
                                }
                            }
                        }

                    }, imageVector = Icons.Rounded.Star, count = videoInfo.favorite,
                color = if (favoriteState.value) bili_50 else Color.Gray
            )

            VerticalIconText(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(5.dp)
                    .clickable {}, imageVector = Icons.Rounded.Share, count = videoInfo.share
            )
        }
    }
}