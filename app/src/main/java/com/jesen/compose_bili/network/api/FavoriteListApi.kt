package com.jesen.compose_bili.network.api

import com.jesen.compose_bili.model.RankingM
import com.jesen.compose_bili.network.BaseApi
import com.jesen.compose_bili.utils.AUTH_TOKEN_K
import com.jesen.compose_bili.utils.AUTH_TOKEN_V
import com.jesen.compose_bili.utils.COURSE_FLAG_K
import com.jesen.compose_bili.utils.COURSE_FLAG_V
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FavoriteListApi : BaseApi {

    @Headers(
        "$AUTH_TOKEN_K: $AUTH_TOKEN_V",
        "$COURSE_FLAG_K: $COURSE_FLAG_V"
    )
    @GET("uapi/fa/favorites")
    suspend fun requestFavoriteList(
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int,
    ): RankingM

    override fun needLogin(): Boolean = true
}