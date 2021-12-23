package com.jesen.biliwebview_module.debug.backpressedtest

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.jesen.common_util_lib.utils.oLog

/**
 * 定义一个发射器结果管理类，
 * 成员包含结果Uri和发射器launcher
 * 调用发射器的launch，将得到结果uri
 * 相当于把二者打包了
 */
class GetContentActivityResult(
    // 入参是String，结果是Uri
    private val launcher: ManagedActivityResultLauncher<String, Uri?>,
    val uri: Uri?
) {
    fun launch(mimeType: String) {
        launcher.launch(mimeType)
    }
}

/**
 * 提供发射器实例
 */
@Composable
fun rememberGetContentActivityResult(): GetContentActivityResult {
    var uri by remember { mutableStateOf<Uri?>(null) }
    // rememberLauncherForActivityResult 是ManagedActivityResultLauncher的子类
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent(), onResult = {
            oLog("rememberGetContentActivityResult, it:$it")
            uri = it
        })
    return remember(launcher, uri) {
        // 构造出GetContentActivityResult实例
        GetContentActivityResult(launcher, uri)
    }
}


/*********************************test :**************************************/
@ExperimentalCoilApi
@Composable
fun ContactPagePhoto() {
    // 拿到构造好的发射器管理对象，点击click的时候调用它的发射方法，结果会回调在第二个成员变量uri中
    val getContent = rememberGetContentActivityResult()
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .layoutId("userImageLayout")
            .fillMaxWidth(1f)
    ) {
        oLog("--ContactPagePhoto, uri: ${getContent.uri}")
        // 拿到结果
        getContent.uri?.let { uri ->
            Image(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxSize(1f),
                contentDescription = "selected image",
                contentScale = ContentScale.Crop,
                painter = rememberImagePainter(data = uri)
            )
        }
    }
    Button(
        onClick = { getContent.launch("image/*") },
        modifier = Modifier.layoutId("getImageBtn")
    ) {
        Text(text = "选取照片")
    }
}

