package com.jesen.compose_bili.ui.pages.delegate

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.jesen.qrzxing.compose.ZxingComposable

/**
 * 扫码页面
 */
@ExperimentalAnimationApi
@Composable
fun DelegateZxingPage() {
    ZxingComposable() { result ->
        Log.d("zx", "result:$result")
    }
}