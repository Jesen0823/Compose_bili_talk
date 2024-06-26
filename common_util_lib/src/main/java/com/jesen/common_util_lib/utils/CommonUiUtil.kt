package com.jesen.common_util_lib.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.BlurTransformation
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.TopAppBar
import com.jesen.common_util_lib.R

fun showToast(context: Context, msg: String) =
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()


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

@ExperimentalCoilApi
@Composable
fun CoilImageBlur(
    url: Any?, modifier: Modifier,
    topLeft: Float = 0f, topRight: Float = 0f,
    bottomLeft: Float = 0f, bottomRight: Float = 0f,
    context: Context = LocalContext.current,
    blurRadius: Float = 2f,
    blurSampling: Float = 2f,
    contentScale: ContentScale = ContentScale.Fit

) {
    Image(
        painter = rememberImagePainter(
            data = url,
            builder = {
                transformations(
                    RoundedCornersTransformation(
                        topLeft = topLeft,
                        topRight = topRight,
                        bottomLeft = bottomLeft,
                        bottomRight = bottomRight
                    ),
                    BlurTransformation(
                        context = context,
                        radius = blurRadius,
                        sampling = blurSampling
                    )
                )
                crossfade(true)
                placeholder(R.drawable.place_img)
                error(R.drawable.place_img)
            },
        ),
        contentScale = contentScale,
        contentDescription = null,
        modifier = modifier,
        alignment = Alignment.Center
    )
}

/**
 * coil图片
 */
@ExperimentalCoilApi
@Composable
fun CoilImage(
    url: Any?, modifier: Modifier,
    topLeft: Float = 0f, topRight: Float = 0f,
    bottomLeft: Float = 0f, bottomRight: Float = 0f,
    allCorner:Float?=0f,
    contentScale: ContentScale = ContentScale.Fit,
) {
    Image(
        painter = rememberImagePainter(
            data = url,
            builder = {
                transformations(
                    RoundedCornersTransformation(
                        topLeft = allCorner?:topLeft,
                        topRight = allCorner?:topRight,
                        bottomLeft = allCorner?:bottomLeft,
                        bottomRight = allCorner?:bottomRight
                    ),
                )
                crossfade(true)
                placeholder(R.drawable.place_img)
                error(R.drawable.place_img)
            },
        ),
        contentDescription = null,
        modifier = modifier,
        alignment = Alignment.Center,
        contentScale = contentScale
    )
}


/**
 * 圆形图片
 */
@ExperimentalCoilApi
@Composable
fun CoilCircleImage(url: Any?, modifier: Modifier) {
    Image(
        painter = rememberImagePainter(
            data = url,
            builder = {
                transformations(
                    CircleCropTransformation()
                )
                fadeIn()
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
fun showAlertDialog(
    titleStr: String?,
    contentStr: String,
    confirmText:String?=null,
    cancelText:String?=null,
    confirmClick: () -> Unit,
    dismissClick: () -> Unit
) {
    val alertDialog = remember { mutableStateOf(true) }
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
            shape = RoundedCornerShape(size = 12.dp),
            // 不设置则不显示
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmClick()
                        alertDialog.value = false
                    }
                ) {
                    Text(text =confirmText?:"确认")
                }
            },
            //用于关闭对话框的按钮。该对话框不会为此按钮设置任何事件，因此它们需要由调用者设置,null则不显示
            dismissButton = {
                TextButton(
                    onClick = {
                        dismissClick()
                        alertDialog.value = false
                    }
                ) {
                    Text(text =cancelText?:"取消")
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
                dismissOnClickOutside = true,
                // 用于在对话框窗口上设置 WindowManager.LayoutParams.FLAG_SECURE 的策略。
                securePolicy = SecureFlagPolicy.Inherit,
                // 对话框内容的宽度是否应限制为平台默认值，小于屏幕宽度。
                usePlatformDefaultWidth = true
            )
        )
    }
}


@Composable
fun BiliTopBar(
    title: @Composable () -> Unit,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
) {
    TopAppBar(
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        elevation = elevation,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        contentPadding = rememberInsetsPaddingValues(
            insets = LocalWindowInsets.current.statusBars,
            applyBottom = false
        )
    )
}