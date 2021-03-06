package com.jesen.biliexoplayer.player

import android.content.Context
import android.widget.Toast
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.video.VideoSize
import com.jesen.common_util_lib.utils.oLog

/**
 * 播放器实例创建
 * */
object ExoPlayerHolder {
    private var exoplayer: ExoPlayer? = null

    fun get(context: Context): ExoPlayer {
        if (exoplayer == null) {
            exoplayer = createExoPlayer(context)
        }
        exoplayer!!.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                ("onPlayerError：${error.errorCode} ,${error.message}")
            }

            override fun onVideoSizeChanged(videoSize: VideoSize) {
                super.onVideoSizeChanged(videoSize)
                oLog("onVideoSizeChanged：${videoSize.width} x ${videoSize.height} | ratio: ${videoSize.pixelWidthHeightRatio}")
            }

            override fun onSurfaceSizeChanged(width: Int, height: Int) {
                super.onSurfaceSizeChanged(width, height)
                oLog("onSurfaceSizeChanged：$width x $height")
            }
        })
        return exoplayer!!
    }

    // 创建ExoPlayer实例
    private fun createExoPlayer(context: Context): ExoPlayer {
        return ExoPlayer.Builder(context)
            .setLoadControl(
                DefaultLoadControl.Builder().setBufferDurationsMs(
                    // 设置预加载上限下限
                    DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                    DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
                    DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS / 10,
                    DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS / 10
                ).build()
            )
            .build()
            .apply {
                // 播放模式，设置为不重复播放
                repeatMode = Player.REPEAT_MODE_ONE
            }
    }
}
