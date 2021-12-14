package com.jesen.compose_bili.repository

import com.jesen.retrofit_lib.api.FavoriteListApi
import com.jesen.retrofit_lib.api.RankingServiceApi


object LazyColumnRepository {

    suspend fun getRankingVideoList(sort: String, pageIndex: Int, pageSize: Int) =
        com.jesen.retrofit_lib.RetrofitClient.createApi(RankingServiceApi::class.java)
            .requestRankingList(sort, pageIndex, pageSize)

    suspend fun getFavoriteList(pageIndex: Int, pageSize: Int) =
        com.jesen.retrofit_lib.RetrofitClient.createApi(FavoriteListApi::class.java)
            .requestFavoriteList(pageIndex, pageSize)
}