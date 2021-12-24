package com.jesen.videodetail_model.ui.widget

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesen.common_util_lib.utils.CoilCircleImage
import com.jesen.common_util_lib.utils.TimeUtil
import com.jesen.common_util_lib.utils.showToast
import com.jesen.retrofit_lib.model.Comment
import com.jesen.videodetail_model.ui.theme.*
import com.jesen.videodetail_model.util.IconTextSmall
import com.jesen.videodetail_model.viewmodel.DetailViewModel


@ExperimentalAnimationApi
@Composable
fun CommentItemUI(
    comment: Comment,
    viewModel: DetailViewModel,
) {
    val context = LocalContext.current
    var isLiked by remember { mutableStateOf(comment.hasLiked) }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .border(
                width = if (isLiked) 1.dp else 0.dp,
                color = if (isLiked) bili_20 else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ),
        color = MaterialTheme.colors.surface,
        elevation = 6.dp
    ) {
        Column(Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 头像
                CoilCircleImage(
                    url = comment.author.avatar,
                    modifier = Modifier
                        .size(62.dp)
                        .padding(12.dp),
                )


                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .width(264.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    // 会员等级设置
                    val vipSetting = when (comment.author.historyCount) {
                        in 200..500 -> 4 to Color.Red
                        in 100..200 -> 3 to goldenrod
                        in 50..100 -> 2 to silver
                        in 10..50 -> 1 to plum
                        else -> 0 to Color.Transparent
                    }
                    // 用户名
                    Text(
                        text = buildAnnotatedString {
                            val stringContext = this
                            withStyle(style = SpanStyle(color = bili_20)) {
                                append(
                                    comment.author.name ?: ""
                                )
                            }
                            vipSetting.let {
                                stringContext.withStyle(
                                    style = SpanStyle(
                                        color = it.second,
                                        fontSize = 14.sp,
                                        fontStyle = FontStyle.Italic,
                                        shadow = Shadow(color = Color.Yellow, blurRadius = 5f)
                                    )
                                ) {
                                    append(if (it.first > 0) "  LV${it.first}" else "")
                                }
                            }

                        },
                        fontSize = 16.sp,
                        color = black87
                    )

                    Text(
                        modifier = Modifier
                            .padding(top = 2.dp),
                        text = TimeUtil.transToYMD(comment.createTime),
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }

                IconTextSmall(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(end = 16.dp, top = 6.dp, bottom = 6.dp)
                        .clickable(enabled = !isLiked) {
                            // 发起点赞评论
                            viewModel.likeComment(comment.commentId.toString()) { ret ->
                                ret?.let {
                                    isLiked = it.status == 200
                                    showToast(context, it.message)
                                }
                            }

                        },
                    imageVector = Icons.Rounded.ThumbUp,
                    color = if (isLiked) bili_50 else gray300,
                    fontSize = 18.sp,
                    iconSize = 24.dp,
                    count = comment.likeCount
                )

            }
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = comment.commentText,
                fontSize = 18.sp,
                color = gray700,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}
