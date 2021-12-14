package com.jesen.videodetail_model.debug

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.biliexoplayer.player.PlayerViewManager
import com.jesen.common_util_lib.utils.oLog
import com.jesen.videodetail_model.ui.page.VideoDetailScreen
import com.jesen.videodetail_model.ui.theme.Compose_bili_talkTheme
import com.jesen.videodetail_model.viewmodel.DetailViewModel

class MainActivity : ComponentActivity() {

    val viewModel by viewModels<DetailViewModel>()

    @ExperimentalAnimationApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videoTest = viewModel.testVideoM


        setContent {
            Compose_bili_talkTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold() {
                        VideoDetailScreen(
                            activity = this@MainActivity,
                            viewModel = viewModel,
                            videoM = videoTest
                        )
                    }
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (newConfig.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                oLog("onConfigurationChanged LANDSCAPE from Configuration， 竖屏转横屏 手动")
                PlayerViewManager.enterFullScreen()
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                oLog("onConfigurationChanged PORTRAIT from Configuration, 横屏转回竖屏")
                PlayerViewManager.exitFullScreen()
            }
        }
    }

    override fun onBackPressed() {
        if (PlayerViewManager.onBackPressed()) return
        super.onBackPressed()
    }
}
