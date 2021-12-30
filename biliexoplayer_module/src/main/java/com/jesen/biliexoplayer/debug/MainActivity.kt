package com.jesen.biliexoplayer.debug

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.jesen.biliexoplayer.player.ExoComposePlayer
import com.jesen.biliexoplayer.ui.theme.Compose_bili_talkTheme

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
                            onBackCall = {}
                        )
                    }
                }
            }
        }
    }
}
