package com.jesen.videodetail_model.debug

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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jesen.common_util_lib.datastore.DataStoreUtil
import com.jesen.common_util_lib.utils.LocalMainActivity
import com.jesen.videodetail_model.ui.page.VideoDetailScreen
import com.jesen.videodetail_model.ui.theme.Compose_bili_talkTheme
import com.jesen.videodetail_model.util.videoDataDemoJson
import com.jesen.videodetail_model.viewmodel.DetailViewModel

class MainActivity : ComponentActivity() {

    val viewModel by viewModels<DetailViewModel>()

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataStoreUtil.init(this)

        // 全屏
        WindowCompat.setDecorFitsSystemWindows(window, false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)


        setContent {
            CompositionLocalProvider(
                LocalMainActivity provides this,
            ) {
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
                                VideoDetailScreen(
                                    videoMjs = videoDataDemoJson,
                                    itemCardClick = {},
                                    onBackCall = {
                                        finish()
                                    }
                                )
                                //TestDanMuButtn()
                            }
                        }

                    }
                }
            }
        }
    }
}
