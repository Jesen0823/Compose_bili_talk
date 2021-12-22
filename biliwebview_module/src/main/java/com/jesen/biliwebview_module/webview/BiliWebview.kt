package com.jesen.biliwebview_module.webview

import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.jesen.biliwebview_module.session.Session
import com.jesen.common_util_lib.ui.theme.bili_50
import com.jesen.common_util_lib.utils.LocalNavController
import com.jesen.common_util_lib.utils.oLog

/**
 * WebView封装
 * @param navController 路由，非必须参数
 * */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BiliWebView(
    modifier: Modifier = Modifier,
    link: String,
    session: Session?,
    navController: NavController? = null,
    onTitleChange: (String) -> Unit
) {
    val context = LocalContext.current
    val curNavController = navController ?: LocalNavController.current

    var progress by remember {
        mutableStateOf(0)
    }

    val webView = remember {
        WebView(context).apply {
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    progress = newProgress
                }

                override fun onReceivedTitle(view: WebView, title0: String) {
                    onTitleChange(title0)
                }
            }
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true

            session?.let {
                CookieManager.getInstance().let { manager ->
                    manager.acceptCookie()
                    manager.acceptThirdPartyCookies(this)
                    manager.setCookie(
                        "api.devio.org",
                        it.toString()
                    )
                }
            }

            loadUrl(link)
        }
    }

    BackHandler {
        if (webView.canGoBack()) {
            oLog("biliWebView, BackHandler---web canBack,and will back")
            webView.goBack()
        } else {
            oLog("biliWebView, BackHandler---will popBackStack")
            curNavController.popBackStack()
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                webView
            }
        )

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.TopCenter),
            visible = progress < 100
        ) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = progress / 100f,
                color = bili_50
            )
        }
    }

    DisposableEffect(key1 = link) {
        onDispose {
            oLog("--onDispose")
            webView.clearCache(true)
            webView.destroy()
        }
    }
}