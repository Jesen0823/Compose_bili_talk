package com.jesen.compose_bili

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jesen.compose_bili.navigation.PageNavHost
import com.jesen.compose_bili.ui.pages.user.LoginPage
import com.jesen.compose_bili.ui.theme.Compose_bili_talkTheme

class MainActivity : ComponentActivity() {

    companion object{
        var pageNavController: NavHostController? = null
    }

    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            pageNavController = rememberNavController()

            Compose_bili_talkTheme {
                // 加入ProvideWindowInsets
                ProvideWindowInsets {
                    // 状态栏改为透明
                    rememberSystemUiController().setStatusBarColor(
                        Color.Transparent, darkIcons = MaterialTheme.colors.isLight
                    )
                    // 底部导航栏
                    rememberSystemUiController().setNavigationBarColor(
                        Color.Transparent, darkIcons = MaterialTheme.colors.isLight
                    )
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
