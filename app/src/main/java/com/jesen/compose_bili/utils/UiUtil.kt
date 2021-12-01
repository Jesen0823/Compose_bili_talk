package com.jesen.compose_bili.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.airbnb.lottie.compose.*
import com.jesen.compose_bili.R
import com.jesen.compose_bili.ui.theme.gray300
import com.jesen.compose_bili.ui.theme.gray700

/**
 * 加载动画
 * */
@Composable
fun LoadingLottieUI(message: String) {

    Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .size(120.dp)
                .background(color = gray300.copy(alpha = 0.8f), shape = RoundedCornerShape(6.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

            ) {

            // 加载文件
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lolitte_circle_loading))
            val progress by animateLottieCompositionAsState(
                composition = composition,
                // 一直播放
                iterations = LottieConstants.IterateForever
            )
            LottieAnimation(
                modifier = Modifier.size(80.dp),
                composition = composition,
                progress = progress
            )

            Text(
                text = message,
                style = TextStyle(fontSize = 22.sp, textAlign = TextAlign.Center, color = gray700)
            )
        }
    }
}

@Composable
fun SnackbarShow(content: String) {
    Snackbar(
        modifier = Modifier
            .wrapContentSize()
            .padding(5.dp),
        shape = RoundedCornerShape(6.dp),
        backgroundColor = Color.Gray.copy(alpha = 0.7f),
        contentColor = Color.White,
        elevation = 4.dp,
        content = { Text(text = content) }
    )
}

/**
 * coil图片
 */
@ExperimentalCoilApi
@Composable
fun CoilImage(url: String?, modifier: Modifier, radius: Float = 4f) {
    Image(
        painter = rememberImagePainter(
            data = url,
            builder = {
                transformations(
                    RoundedCornersTransformation(radius)
                )
                placeholder(R.drawable.place_img)
                error(R.drawable.place_img)
            },
        ),
        contentDescription = null,
        modifier = modifier,
        alignment = Alignment.Center
    )
}


/**
 * 圆形图片
 */
@ExperimentalCoilApi
@Composable
fun CoilCircleImage(url: String?, modifier: Modifier) {
    Image(
        painter = rememberImagePainter(
            data = url,
            builder = {
                transformations(
                    CircleCropTransformation()
                )
                placeholder(R.drawable.place_head)
                error(R.drawable.place_head)
            },
        ),
        contentDescription = null,
        modifier = modifier,
        alignment = Alignment.Center,
    )
}


/**
 * AlertDialog弹窗
 */
@ExperimentalComposeUiApi
@Composable
private fun showAlertDialog(
    titleStr: String?,
    contentStr: String,
    confirmClick: () -> Unit,
    dismissClick: () -> Unit
) {
    val alertDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    if (alertDialog.value) {
        AlertDialog(
            // 外部点击是否关闭
            onDismissRequest = {
                alertDialog.value = false
            },
            title = {
                titleStr?.let { Text(text = it) }
            },
            text = {
                Text(contentStr)
            },
            // 不设置则不显示
            confirmButton = {
                TextButton(
                    onClick = confirmClick
                ) {
                    Text("确认")
                }
            },
            //用于关闭对话框的按钮。该对话框不会为此按钮设置任何事件，因此它们需要由调用者设置,null则不显示
            dismissButton = {
                TextButton(
                    onClick = dismissClick
                ) {
                    Text("取消")
                }
            },
            backgroundColor = MaterialTheme.colors.background,
            // 首选内容颜色
            contentColor = MaterialTheme.colors.onSecondary,
            // 平台特定的属性，以进一步配置对话框
            properties = DialogProperties(
                // 是否可以通过按下后退按钮来关闭对话框。 如果为 true，按下后退按钮将调用 onDismissRequest。
                dismissOnBackPress = true,
                // 是否可以通过在对话框边界外单击来关闭对话框。 如果为 true，单击外部将调用 onDismissRequest
                dismissOnClickOutside = false,
                // 用于在对话框窗口上设置 WindowManager.LayoutParams.FLAG_SECURE 的策略。
                securePolicy = SecureFlagPolicy.Inherit,
                // 对话框内容的宽度是否应限制为平台默认值，小于屏幕宽度。
                usePlatformDefaultWidth = true
            )
        )
    }
}
