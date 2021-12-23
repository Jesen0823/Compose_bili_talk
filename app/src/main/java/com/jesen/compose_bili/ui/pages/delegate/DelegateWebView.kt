package com.jesen.compose_bili.ui.pages.delegate

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

/**
 * WebView页面封装
 */
@Composable
fun DelegateWebView(url: String) {
    val navController = LocalNavController.current
    var title by remember {
        mutableStateOf("")
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
            link = url,
            session = SessionManager.session
        ) {
            title = it
        }
    }
}
