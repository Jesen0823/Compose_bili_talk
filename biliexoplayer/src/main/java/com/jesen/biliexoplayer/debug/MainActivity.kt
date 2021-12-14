package com.jesen.biliexoplayer.debug

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.jesen.biliexoplayer.player.ExoComposePlayer
import com.jesen.biliexoplayer.player.PlayerViewManager
import com.jesen.biliexoplayer.ui.theme.Compose_bili_talkTheme
import com.jesen.common_util_lib.utils.oLog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Compose_bili_talkTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold() {
                        ExoComposePlayer(
                            activity = this,
                            modifier = Modifier.fillMaxWidth(),
                            title = "测试测试测试",
                            url = "https://o.devio.org/files/video/v=eiDiKwbGfIY.mp4",
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
