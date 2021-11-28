package com.jesen.compose_bili

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jesen.compose_bili.ui.theme.Compose_bili_talkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
                    Surface(color = MaterialTheme.colors.background) {

                    }
                }
            }
        }
    }
}
