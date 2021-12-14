package com.jesen.videodetail_model.ui.widget

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.jesen.retrofit_lib.model.VideoDetailData
import com.jesen.videodetail_model.viewmodel.DetailViewModel

@Composable
fun VideoDesContent(viewModel: DetailViewModel, data: VideoDetailData) {
    Text(data.videoInfo.desc)
}