package com.jesen.compose_bili.repository

import com.jesen.retrofit_lib.RetrofitClient
import com.jesen.retrofit_lib.api.NoticeListApi

object NoticeListRepository {

    suspend fun requestNotices(pageIndex: Int, pageSize: Int) =
        RetrofitClient.createApi(NoticeListApi::class.java)
            .requestNotice(pageIndex, pageSize)
}