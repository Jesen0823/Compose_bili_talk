package com.jesen.videodetail_model.debug

import android.content.res.Configuration
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jesen.biliexoplayer.player.PlayerViewManager
import com.jesen.common_util_lib.utils.oLog
import com.jesen.videodetail_model.ui.theme.Compose_bili_talkTheme
import com.jesen.videodetail_model.viewmodel.DetailViewModel

class MainActivity : ComponentActivity() {

    val viewModel by viewModels<DetailViewModel>()

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 全屏
        WindowCompat.setDecorFitsSystemWindows(window, false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val videoTest = viewModel.testVideoM


        setContent {
            // 处理状态栏
            ProvideWindowInsets {
                Compose_bili_talkTheme {
                    val systemUiController = rememberSystemUiController()
                    val primaryColor = MaterialTheme.colors.background
                    val dark = MaterialTheme.colors.isLight
                    // A surface container using the 'background' color from the theme
                    Surface(color = MaterialTheme.colors.background) {
                        // 设置系统栏
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
                        Scaffold() {
                            /* VideoDetailScreen(
                                 activity = this@MainActivity,
                                 viewModel = viewModel,
                                 videoM = videoTest
                             )*/
                            TestDanMuButtn()
                        }
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
