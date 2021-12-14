package com.jesen.retrofit_lib.api

import com.jesen.retrofit_lib.com.AUTH_TOKEN_K
import com.jesen.retrofit_lib.com.AUTH_TOKEN_V
import com.jesen.retrofit_lib.com.COURSE_FLAG_K
import com.jesen.retrofit_lib.com.COURSE_FLAG_V
import com.jesen.retrofit_lib.model.RankingM
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FavoriteListApi : com.jesen.retrofit_lib.BaseApi {

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