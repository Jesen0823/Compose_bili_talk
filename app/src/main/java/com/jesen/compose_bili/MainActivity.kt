package com.jesen.compose_bili

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jesen.compose_bili.navigation.PageNavHost
import com.jesen.compose_bili.ui.theme.Compose_bili_talkTheme

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {

    companion object {
        var pageNavController: NavHostController? = null
    }

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            pageNavController = rememberNavController()
            // 可定义动画的Navigation库
            //pageNavController = rememberAnimatedNavController()
            // 加入ProvideWindowInsets
            ProvideWindowInsets {
                Compose_bili_talkTheme {

                    val systemUiController = rememberSystemUiController()
                    val primaryColor = MaterialTheme.colors.background
                    val dark = MaterialTheme.colors.isLight

                    // 状态栏改为透明
                    SideEffect {
                        systemUiController.setStatusBarColor(
                            Color.Transparent, darkIcons = dark
                        )
                        // 底部导航栏
                        systemUiController.setNavigationBarColor(
                            primaryColor, darkIcons = dark
                        )

                    }

                    // A surface container using the 'background' color from the theme
                    Surface(
                        color = MaterialTheme.colors.background,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        PageNavHost(this)
                    }
                }
            }
        }
    }
}
