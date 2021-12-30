package com.jesen.biliexoplayer.player

import android.net.Uri
import android.util.Log
import android.util.SizeF
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavBackStackEntry
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.BiliStyledPlayerView
import com.google.android.exoplayer2.util.Util
import com.jesen.biliexoplayer.player.PlayerViewManager.TOP_BACK_TAG
import com.jesen.biliexoplayer.player.cache.VideoDataSourceHolder
import com.jesen.common_util_lib.utils.oLog


/**
 * 简单播放器封装
 * */
@Composable
fun ExoComposePlayer(
    activity: ComponentActivity,
    modifier: Modifier = Modifier,
    url: String?,
    onBackCall: () -> Unit,
    size: SizeF = SizeF(1280f, 720f),
    autoPlayVideo: Boolean = true,
    autoPlayOnWifi: Boolean = true,
    title: String
) {
    val context = LocalContext.current

    PlayerViewManager.init(activity = activity)

    // 获取播放器实例
    val exoPlayer = remember { ExoPlayerHolder.get(context = context) }

    val playerView = remember {
        PlayerViewManager.get(context)
    }

    BackHandler() {
        oLog("--ComposePlayer, BackHandler")
        if (playerView.playerController.isFullScreen) {
            playerView.playerController.switchFullScreenMode()
        } else {
            onBackCall()
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
        modifier = modifier
            .aspectRatio(size.width / size.height)
            .wrapContentHeight(),
        factory = { context ->
            oLog("--ComposePlayer,factory")
            val frameLayout = FrameLayout(context)
            frameLayout.setBackgroundColor(context.getColor(android.R.color.background_dark))
            frameLayout.removeAllViews()

            // 切换播放器
            BiliStyledPlayerView.switchTargetView(
                exoPlayer,
                PlayViewData.instance.curPlayerView,
                playerView
            )
            PlayViewData.instance.curPlayerView = playerView

            playerView.apply {
                player?.playWhenReady =
                    autoPlayVideo || (autoPlayOnWifi)
                setControllerOnExtendButtonEventListener { button ->
                    Log.d("Manager--", "button:$button")
                    if (button.tag.equals(TOP_BACK_TAG)) {
                        onBackCall()
                    }
                }
            }

            frameLayout.addView(
                playerView,
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            playerView.let {
                PlayerViewManager.setVideoTitleContent(it, title)
            }
            frameLayout
        },
        update = {

        }

    )

    OnLifecycleEvent { owner, event ->
        if (owner is NavBackStackEntry) {
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    exoPlayer.pause()
                }
                Lifecycle.Event.ON_RESUME -> {
                    exoPlayer.play()
                }
                else -> {
                }
            }
        }
    }

    DisposableEffect(key1 = url) {
        onDispose {
            oLog("--ComposePlayer,DisposableEffect--onDispose")
            exoPlayer.stop()
            PlayViewData.instance.curPlayerView = null
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
