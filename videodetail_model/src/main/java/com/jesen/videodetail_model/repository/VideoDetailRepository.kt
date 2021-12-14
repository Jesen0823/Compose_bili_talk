package com.jesen.videodetail_model.repository

import com.jesen.retrofit_lib.RetrofitClient
import com.jesen.retrofit_lib.api.VideoDetailApi

object VideoDetailRepository {

    suspend fun getVideDetailData(vid: String) =
        RetrofitClient.createApi(VideoDetailApi::class.java)
            .requestVideoDetail(videoId = vid)
}