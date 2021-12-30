package com.jesen.biliexoplayer.player

import com.google.android.exoplayer2.ui.BiliStyledPlayerView

class PlayViewData private constructor() {

    var curPlayerView: BiliStyledPlayerView? = null

    companion object {
        val instance: PlayViewData by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            PlayViewData()
        }
    }
}
