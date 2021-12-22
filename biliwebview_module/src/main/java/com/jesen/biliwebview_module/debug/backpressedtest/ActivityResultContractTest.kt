package com.jesen.biliwebview_module.debug.backpressedtest

import MutilPermissionTest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * androidx/activity/result/contract/ActivityResultContracts.kt
 * */


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun TestActivityResultContract() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        MutilPermissionTest()
        SinglePermissionTest()
        TestBackPressed()
        TakePicTest()
    }
}