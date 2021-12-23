package com.jesen.biliwebview_module.debug

import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jesen.biliwebview_module.debug.backpressedtest.TestActivityResultContract
import com.jesen.biliwebview_module.debug.localprovidertest.Book
import com.jesen.biliwebview_module.ui.theme.Compose_bili_talkTheme
import com.jesen.common_util_lib.datastore.DataStoreUtil
import com.jesen.common_util_lib.utils.LocalNavController

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataStoreUtil.init(this)

        // 全屏
        WindowCompat.setDecorFitsSystemWindows(window, false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val userNew = Book(
            name = "金瓶梅",
            photoUrl = "https://bkimg.cdn.bcebos.com/pic/0eb30f2442a7d9333a74267fad4bd11372f001da?x-bce-process=image/resize,m_lfit,w_268,limit_1/format,f_auto"
        )

        setContent {
            CompositionLocalProvider(
                // 这是测试页面，设定一个默认值防止有使用NavController的地方异常
                LocalNavController provides NavController(context = this.applicationContext),
                //ActiveUser provides userNew
            ) {
                ProvideWindowInsets {
                    Compose_bili_talkTheme {
                        val systemUiController = rememberSystemUiController()
                        val primaryColor = MaterialTheme.colors.background
                        val dark = MaterialTheme.colors.isLight

                        // set ui color
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

                        // A surface container using the 'background' color from the theme
                        Surface(color = MaterialTheme.colors.background) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Spacer(modifier = Modifier.statusBarsHeight())
                                TestActivityResultContract()

                                //TestWebView()


                                //TestEnterScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}
