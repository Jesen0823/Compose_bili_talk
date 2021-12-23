package com.jesen.biliwebview_module.debug.backpressedtest

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.jesen.biliwebview_module.debug.utils.FileUtil
import com.jesen.common_util_lib.utils.oLog
import com.jesen.common_util_lib.utils.showToast


/**
 * 在底层，它创建了一个OnBackPressedCallback，并在可组合函数成功重新组合时启用它。回调在任何生命周期所有者更改时被添加，
 * 并在dispose中被删除。所有的东西都是隐式设置的，我们需要做的就是添加一行代码，它声明了BackHandler():
 * @Composable
fun Detail(
state: DetailState,
onBack: () -> Unit,
) {
BackHandler(onBack = onBack)
Scaffold(
topBar = { DetailTopAppBar(onBack) }
) {
// detail content
}
}
 *
 * */
@Composable
fun TestBackPressed() {
    // 返回按键
    var backPressedCount by remember { mutableStateOf(0) }
    BackHandler { backPressedCount++ }

    val dispatcher = LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher

    Button(onClick = { dispatcher.onBackPressed() }) {
        Text("返回按键测试： $backPressedCount")
    }
}


@Composable
fun SinglePermissionTest() {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            oLog("request permission success.")
            showToast(context, "权限申请成功")
        } else {
            oLog("request permission failed")
            showToast(context, "权限申请被拒绝")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Button(
            onClick = {
                // 检查权限
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) -> {
                        showToast(context, "权限访问正常哦")
                        oLog("permission is ok")
                    }
                    else -> {
                        // 请求权限
                        launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
            }
        ) {
            Text(text = "检查并申请单一权限")
        }
    }
}

@Composable
fun TakePicPreviewTest() {
    val result = remember { mutableStateOf<Bitmap?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        result.value = it
    }

    Button(onClick = { launcher.launch() }) {
        Text(text = "便捷拍照预览")
    }

    result.value?.let { image ->
        Image(image.asImageBitmap(), null, modifier = Modifier.fillMaxWidth())
    }
}

@ExperimentalCoilApi
@Composable
fun TakePicTest() {
    val context = LocalContext.current
    val fileUri = FileUtil.getImgUri(context)
    oLog("TakePicTest, fileUri:$fileUri")
    val result = remember { mutableStateOf<Boolean>(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        result.value = it
    }

    Button(onClick = { launcher.launch(fileUri) }) {
        Text(text = "便捷拍照")
    }

    result.value.let { success ->
        oLog("TakePicTest, is success:$success")
        if (success) {
            Image(
                painter = rememberImagePainter(data = Uri.parse(fileUri.encodedPath)),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )

            // CoilImage(url = Uri.parse(fileUri.encodedPath), modifier = Modifier.fillMaxWidth())
        }
    }
}
