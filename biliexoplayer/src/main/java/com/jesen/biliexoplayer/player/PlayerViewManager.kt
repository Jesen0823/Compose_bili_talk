package com.jesen.biliexoplayer.player

import android.content.Context
import android.content.pm.ActivityInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.core.util.Pools
import com.airbnb.lottie.LottieAnimationView
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.BiliPlayerView
import com.jesen.biliexoplayer.R
import com.jesen.common_util_lib.utils.oLog
import com.jesen.common_util_lib.utils.statusBarIsHide

/**
 * 用来管理 PlayerView
 * */
object PlayerViewManager : ExoEventListener {

    // 播放模式，是单一播放/列表播放
    var playerMode = PlayerMode.SINGLE_MODE

    var playerViewMode = PlayViewMode.HALF_SCREEN
    var activity: ComponentActivity? = null


    private val playerViewPool = Pools.SimplePool<BiliPlayerView>(2)

    fun get(context: Context): BiliPlayerView {
        return playerViewPool.acquire() ?: createPlayerView(context)
    }

    fun release(player: BiliPlayerView) {
        playerViewPool.release(player)
    }

    /**
     * 创建PlayerView
     * */
    private fun createPlayerView(context: Context): BiliPlayerView {
        val playView = (LayoutInflater.from(context)
            .inflate(R.layout.exoplayer_texture_view, null, false) as BiliPlayerView)
        // 自定义加载动画
        val loading = playView.findViewById<LottieAnimationView>(R.id.bili_loading)
        playView.bufferingViewBili = loading
        playView.apply {
            setShowMultiWindowTimeBar(true)
            setShowBuffering(BiliPlayerView.SHOW_BUFFERING_ALWAYS)
            controllerAutoShow = true
            playerController.setExoEventListener(this@PlayerViewManager)
            keepScreenOn = true
        }

        initOther(playView)
        return playView
    }


    /****************************************其他业务*******************************************/


    private fun initOther(playView: BiliPlayerView) {
        PlayViewData.instance.activity = activity

        // 返回按钮
        val backExitBtn = playView.findViewById<ImageView>(R.id.back_play)
        backExitBtn.setOnClickListener {
            if (isFullScreen()) {
                switchPlayerViewMode()
            } else {
                PlayViewData.instance.activity?.finish()
            }
        }
        playView.playerController.videoTitle = playView.findViewById(R.id.v_title)
    }


    fun switchPlayerViewMode() {
        oLog("switchPlayerViewMode")
        val activity = PlayViewData.instance.activity
        if (activity?.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            //切换竖屏
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            //切换横屏
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    fun setVideoTitle(visibility: Boolean, content: String? = "") {
        val curPlayView = PlayViewData.instance.curPlayerView
        val title = curPlayView?.playerController?.videoTitle
        if (content.isNullOrBlank()) {
            title?.visibility = if (visibility) View.VISIBLE else View.GONE
        } else {
            title?.text = content
        }
    }

    fun enterFullScreen() {
        val activity = PlayViewData.instance.activity
        val contentRoot = activity?.findViewById<ViewGroup>(android.R.id.content)
        val curPlayView = PlayViewData.instance.curPlayerView
        // 保存竖屏下的播放器父布局
        val curParent = curPlayView?.parent as ViewGroup

        // 隐藏状态栏导航栏
        activity?.statusBarIsHide(contentRoot as View, true)

        // 从curParent移到rootContent
        curParent.removeView(curPlayView)
        contentRoot?.addView(
            curPlayView, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        oLog(" enterFullScreen,  remove parent:$curParent, add contentView:$contentRoot")
        setVideoTitle(true)
        PlayViewData.instance.fullPlayParent = contentRoot
        playerViewMode = PlayViewMode.FULL_SCREEN
    }

    fun exitFullScreen(): Boolean {
        if (isFullScreen()) {
            oLog("switchPlayerViewMode  set to screen half")
            val activity = PlayViewData.instance.activity
            val rootContent = PlayViewData.instance.fullPlayParent
            val curPlayView = PlayViewData.instance.curPlayerView
            rootContent?.let {
                // 恢复状态栏显示
                activity?.statusBarIsHide(it as View, false)
                // 从根rootContent移除PlayerView
                it.removeView(curPlayView)
            }

            val originParent = PlayViewData.instance.halfPlayParent
            // 然后加入LazyColumn的ItemView下
            originParent?.addView(
                curPlayView,
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setVideoTitle(false)

            playerViewMode = PlayViewMode.HALF_SCREEN

            return true
        }
        return false
    }


    /**
     * 全屏处理
     * */
    override fun changeFullScreen(player: Player) {
        switchPlayerViewMode()
    }

    override fun backExitScreen(player: Player) {
        if (isFullScreen()) {
            exitFullScreen()
        } else {
            activity?.finish()
        }
    }

    /**
     * 暂停续播
     * */
    fun playOrPause(isPause: Boolean) {
        val curViewPlay = PlayViewData.instance.curPlayerView
        val playerController = curViewPlay?.playerController
        playerController?.let {
            if (isPause) it.doPause() else it.doPlay()
        }
    }

    private fun isFullScreen(): Boolean = playerViewMode == PlayViewMode.FULL_SCREEN

    fun onBackPressed(): Boolean {
        return exitFullScreen()
    }
}


enum class PlayViewMode { HALF_SCREEN, FULL_SCREEN }

enum class PlayerMode { SINGLE_MODE, LIST_MODE }
