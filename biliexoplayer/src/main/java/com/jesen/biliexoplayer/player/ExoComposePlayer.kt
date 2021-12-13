package com.jesen.biliexoplayer.player

import android.net.Uri
import android.util.SizeF
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavBackStackEntry
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.BiliPlayerView
import com.google.android.exoplayer2.util.Util
import com.jesen.biliexoplayer.player.cache.VideoDataSourceHolder
import com.jesen.common_util_lib.utils.isFreeNetwork
import com.jesen.common_util_lib.utils.oLog

/**
 * 简单播放器封装
 * */
@Composable
fun ExoComposePlayer(
    modifier: Modifier = Modifier,
    title: String,
    url: String?,
    size: SizeF = SizeF(1280f, 720f),
    autoPlayVideo: Boolean = true,
    autoPlayOnWifi: Boolean = true
) {
    val context = LocalContext.current

    // 获取播放器实例
    val exoPlayer = remember { ExoPlayerHolder.get(context = context) }
    var playerView: BiliPlayerView? = null

    val systemUiController = rememberSystemUiController()
    val primaryColor = MaterialTheme.colors.background
    val dark = MaterialTheme.colors.isLight
    SideEffect {
        systemUiController.setNavigationBarColor(
            primaryColor,
            darkIcons = dark
        )
        systemUiController.setStatusBarColor(
            Color.Transparent,
            darkIcons = dark
        )
    }

    BackHandler(
        enabled = PlayerViewManager.playerViewMode == PlayViewMode.HALF_SCREEN
    ) {
        oLog("--ComposePlayer, BackHandler")
        PlayerViewManager.switchPlayerViewMode()
    }

    OnLifecycleEvent { owner, event ->
        if (owner is NavBackStackEntry) {
            oLog("--ComposePlayer,OnLifecycleEvent event:${event.name}")
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    PlayerViewManager.playOrPause(true)
                }
                Lifecycle.Event.ON_RESUME -> {
                    PlayerViewManager.playOrPause(false)
                }
                Lifecycle.Event.ON_STOP -> {

                }
                else -> {
                }
            }
        }
    }

    url?.let {
        LaunchedEffect(key1 = url) {
            val playUri = Uri.parse(url)
            val dataSourceFactory = VideoDataSourceHolder.getCacheFactory(context)
            val mediaSource = when (Util.inferContentType(playUri)) {
                C.TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(playUri))
                C.TYPE_HLS -> HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(playUri))
                else -> ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(playUri))
            }

            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()
        }

    }

    AndroidView(
        modifier = Modifier.aspectRatio(size.width / size.height),
        factory = { context ->
            oLog("--ComposePlayer,factory")
            val frameLayout = FrameLayout(context)
            frameLayout.setBackgroundColor(context.getColor(android.R.color.background_dark))
            frameLayout
        },
        update = { frameLayout ->
            oLog("--ComposePlayer, update,frameLayout:$frameLayout")

            if (playerUIEnable()) {
                frameLayout.removeAllViews()
                playerView = PlayerViewManager.get(frameLayout.context)

                // 切换播放器
                BiliPlayerView.switchTargetView(
                    exoPlayer,
                    PlayViewData.instance.curPlayerView,
                    playerView
                )

                PlayViewData.instance.curPlayerView = playerView
                PlayerViewManager.setVideoTitle(true, title)

                playerView?.apply {
                    player?.playWhenReady =
                        autoPlayVideo || (autoPlayOnWifi && context.isFreeNetwork())
                }

                playerView?.apply {
                    (parent as? ViewGroup)?.removeView(this)
                }
                frameLayout.addView(
                    playerView,
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                PlayViewData.instance.halfPlayParent = frameLayout
            }
        }
    )

    DisposableEffect(key1 = url) {
        onDispose {
            oLog("--ComposePlayer,DisposableEffect--onDispose")

            playerView?.apply {
                (parent as? ViewGroup)?.removeView(this)
            }
            exoPlayer.stop()
            playerView?.let {
                PlayerViewManager.release(it)
            }
            playerView = null
        }
    }
}

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

fun playerUIEnable(): Boolean = when (PlayerViewManager.playerMode) {
    PlayerMode.SINGLE_MODE -> true
    PlayerMode.LIST_MODE -> PlayerViewManager.playerViewMode == PlayViewMode.HALF_SCREEN
}