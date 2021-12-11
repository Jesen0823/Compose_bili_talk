package com.jesen.compose_bili.ui.pages.mainchildren

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jesen.compose_bili.MainActivity

/**
 * 收藏页面
 * */
@Composable
fun FavoritePage(activity: MainActivity) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "fav")
    }
}