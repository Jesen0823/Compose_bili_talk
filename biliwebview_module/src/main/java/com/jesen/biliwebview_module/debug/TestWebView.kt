package com.jesen.biliwebview_module

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jesen.biliwebview_module.session.SessionManager
import com.jesen.biliwebview_module.webview.BiliWebView
import com.jesen.common_util_lib.utils.BiliTopBar
import com.jesen.common_util_lib.utils.LocalNavController

@Composable
fun TestWebView(
) {
    val navController = LocalNavController.current
    var title by remember {
        mutableStateOf("私聊消息")
    }
    Scaffold(topBar = {
        BiliTopBar(
            title = {
                Text(text = title)
            },
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(Icons.Default.ArrowBack, null)
                }
            }, elevation = 12.dp
        )

    }) {
        BiliWebView(
            modifier = Modifier.fillMaxSize(),
            link = "https://docs.compose.net.cn/",
            session = SessionManager.session
        ) {
            title = it
        }
    }
}
