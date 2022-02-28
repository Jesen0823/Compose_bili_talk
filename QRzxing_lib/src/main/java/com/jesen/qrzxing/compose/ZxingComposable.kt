package com.jesen.qrzxing.compose

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.jesen.qrzxing.QrCodeAnalyzer

@Composable
fun ZxingComposable(resultBack: (String) -> Unit) {

    var code by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }

    val tipsTopState  = remember{ mutableStateOf(false)}

    var hasCameraPremission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPremission = granted
        }
    )

    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }
    // 拿到返回键分发Owner
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Box(modifier = Modifier.fillMaxSize()) {
        // 检查相机权限
        if (hasCameraPremission) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    val previewView = PreviewView(context)
                    val preview = Preview.Builder().build()
                    val selector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()
                    preview.setSurfaceProvider(previewView.surfaceProvider)
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setTargetResolution(
                            Size(previewView.width, previewView.height)
                        )
                        .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST).build()
                    imageAnalysis.setAnalyzer(
                        ContextCompat.getMainExecutor(context),
                        QrCodeAnalyzer { result ->
                            code = result
                            tipsTopState.value = true
                            resultBack(code)
                        }
                    )

                    try {
                        cameraProviderFuture.get().bindToLifecycle(
                            lifecycleOwner,
                            selector,
                            preview,
                            imageAnalysis
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    previewView
                },
            )

            ScanDecoration(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Transparent)
            )
        }
        Row(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.background(color = Color.Transparent),
                onClick = { dispatcher?.onBackPressed() },
            ) {
                Icon(
                    modifier = Modifier.background(color = Color.Transparent),
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "back",
                    tint = Color.White
                )
            }
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .background(color = Color.Transparent)
                    .padding(start = 30.dp),
                text = if (tipsTopState.value) "扫描完成" else "正在努力扫描中...",
                color = if (tipsTopState.value) Color.Green else Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }

}