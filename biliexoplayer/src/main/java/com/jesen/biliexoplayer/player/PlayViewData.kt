package com.jesen.biliexoplayer.player

import android.view.ViewGroup
import androidx.activity.ComponentActivity
import com.google.android.exoplayer2.ui.BiliPlayerView

class PlayViewData private constructor() {

    // 全屏半屏切换前的父布局
    var halfPlayParent: ViewGroup? = null
    var fullPlayParent: ViewGroup? = null

    var curPlayerView: BiliPlayerView? = null
    var activity: ComponentActivity? = null

    companion object {
        val instance: PlayViewData by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            PlayViewData()
        }
    }
}
