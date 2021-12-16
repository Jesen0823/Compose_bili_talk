package com.jesen.compose_bili.repository

import com.jesen.retrofit_lib.RetrofitClient
import com.jesen.retrofit_lib.api.HomeServiceApi

/**
 * 首页列表请求 Repository
 * */
object HomeCategoryRepository {
    suspend fun requestHome(category: String, pageIndex: Int, pageSize: Int) =
        RetrofitClient.createApi(HomeServiceApi::class.java)
            .requestHomeCategory(category, pageIndex, pageSize)
}
