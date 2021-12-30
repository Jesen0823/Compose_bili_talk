package com.jesen.biliexoplayer.player

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.core.util.Pools
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.airbnb.lottie.LottieAnimationView
import com.google.android.exoplayer2.ui.BiliStyledPlayerView
import com.jesen.biliexoplayer.R

/**
 * 用来管理 PlayerView
 * */
object PlayerViewManager {

    private lateinit var curActivity: ComponentActivity
    const val TOP_BACK_TAG = "top_back"

    // 当前全屏半屏状态记录
    private var currIsFullScreen = false

    private var layoutParamLast: ViewGroup.LayoutParams? = null

    fun init(activity: ComponentActivity) {
        curActivity = activity
    }

    private val playerViewPool = Pools.SimplePool<BiliStyledPlayerView>(2)

    fun get(context: Context): BiliStyledPlayerView {
        return playerViewPool.acquire() ?: createPlayerView(context)
    }

    fun release(player: BiliStyledPlayerView) {
        playerViewPool.release(player)
    }

    /**
     * 创建PlayerView
     * */
    private fun createPlayerView(context: Context): BiliStyledPlayerView {

        val playView = (LayoutInflater.from(context)
            .inflate(R.layout.exoplayer_texture_view, null, false) as BiliStyledPlayerView)

        initOther(playView)

        val bufferLottie = playView.findViewById<LottieAnimationView>(R.id.exo_cnm_buffering);
        playView.apply {
            bufferingViewBili = bufferLottie
            setShowMultiWindowTimeBar(true)
            setShowBuffering(BiliStyledPlayerView.SHOW_BUFFERING_ALWAYS)
            controllerAutoShow = true
            keepScreenOn = true
            setControllerOnFullScreenModeChangedListener { isFullScreen -> //TODO("Not yet implemented")
                Log.d("Manager--", "isFull:$isFullScreen")
                switchIsFullScreen(isFullScreen, this)
            }
        }
        return playView
    }

    private fun initOther(playView: BiliStyledPlayerView) {
        val controllerView = playView.playerController
        controllerView.setBiliVideoTitle(controllerView.findViewById(R.id.v_title))
        val backTopBtn = controllerView.findViewById<ImageView>(R.id.back_play)
        backTopBtn.tag = TOP_BACK_TAG
        controllerView.setBiliBackButton(backTopBtn)
    }

    public fun setVideoTitleContent(playView: BiliStyledPlayerView, title: String) {
        val controllerView = playView.playerController
        controllerView.getBiliVideoTitle().text = title
    }

    private fun switchIsFullScreen(
        isFullScreen: Boolean,
        playerView: BiliStyledPlayerView
    ) {
        val controller = ViewCompat.getWindowInsetsController(curActivity.window.decorView)
        currIsFullScreen = isFullScreen
        if (isFullScreen) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                curActivity.window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            } else {
                curActivity.window.setDecorFitsSystemWindows(false)
                controller?.hide(WindowInsetsCompat.Type.statusBars())
                controller?.hide(WindowInsetsCompat.Type.navigationBars())
                controller?.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
            val layoutParam = playerView.layoutParams
            layoutParamLast = layoutParam
            Log.d("PlayerManager--", "layoutParam height=${layoutParam.height}")
            curActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            layoutParam.height = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParam.width = ViewGroup.LayoutParams.MATCH_PARENT
            playerView.layoutParams = layoutParam
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                curActivity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            } else {
                curActivity.window.setDecorFitsSystemWindows(true)
                controller?.show(WindowInsetsCompat.Type.statusBars())
                controller?.show(WindowInsetsCompat.Type.navigationBars())
            }

            val layoutParam = playerView.layoutParams
            layoutParam.height =
                (230 * curActivity.application.resources.displayMetrics.density).toInt()
            layoutParam.width = ViewGroup.LayoutParams.MATCH_PARENT

            val firstLayoutParams = layoutParamLast
            curActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            playerView.layoutParams = firstLayoutParams ?: layoutParam
        }
    }

}
/****************************************其他业务*******************************************/



   

    