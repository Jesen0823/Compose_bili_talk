package com.jesen.compose_bili.repository

import com.jesen.compose_bili.network.RetrofitClient
import com.jesen.compose_bili.network.api.RankingServiceApi


object LazyColumnRepository {

    suspend fun getRankingVideoList(sort: String, pageIndex: Int, pageSize: Int) =
        RetrofitClient.createApi(RankingServiceApi::class.java)
            .requestRankingList(sort, pageIndex, pageSize)
}