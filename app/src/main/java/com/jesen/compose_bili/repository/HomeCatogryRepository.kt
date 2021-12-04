package com.jesen.compose_bili.repository

import com.jesen.compose_bili.network.HomeServiceApi
import com.jesen.compose_bili.network.RetrofitClient

/**
 * 首页列表请求 Repository
 * */
object HomeCategoryRepository {

    suspend fun requestHome(category: String, pageIndex: Int, pageSize: Int) =
        RetrofitClient.createApi(HomeServiceApi::class.java)
            .requestHomeCategory(category, pageIndex, pageSize)
}
