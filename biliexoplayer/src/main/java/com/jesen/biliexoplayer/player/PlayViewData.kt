package com.jesen.biliexoplayer.player

import android.view.ViewGroup
import com.google.android.exoplayer2.ui.BiliPlayerView
import com.jesen.biliexoplayer.debug.MainActivity

class PlayViewData private constructor() {

    // 全屏半屏切换前的父布局
    var halfPlayParent: ViewGroup? = null
    var fullPlayParent: ViewGroup? = null

    var curPlayerView: BiliPlayerView? = null
    var activity: MainActivity? = null

    companion object {
        val instance: PlayViewData by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            PlayViewData()
        }
    }
}
