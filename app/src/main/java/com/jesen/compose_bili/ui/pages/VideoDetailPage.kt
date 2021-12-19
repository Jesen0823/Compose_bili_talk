package com.jesen.compose_bili.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.navigationBarsWithImePadding

//import com.jesen.biliexoplayer.player.ExoComposePlayer

/**
 * 视频详情页
 * */
@Composable
fun VideoDetailPage(param: String?) {
    Scaffold() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsWithImePadding()
        ) {
            /* ExoComposePlayer(
                 modifier = Modifier
                     .fillMaxWidth()
                     .aspectRatio(16 / 9f),
                 title = "测试播放器。。。",
                 url = "https://o.devio.org/files/video/v=eiDiKwbGfIY.mp4"
             )*/
        }
    }
}

