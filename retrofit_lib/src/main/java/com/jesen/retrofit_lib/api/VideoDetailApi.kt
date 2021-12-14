package com.jesen.retrofit_lib.api

import com.jesen.retrofit_lib.com.AUTH_TOKEN_K
import com.jesen.retrofit_lib.com.AUTH_TOKEN_V
import com.jesen.retrofit_lib.com.COURSE_FLAG_K
import com.jesen.retrofit_lib.com.COURSE_FLAG_V
import com.jesen.videodetail_model.model.VideoDetailM
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * 视频详情页
 * [Flut] 请求url:https://api.devio.org/uapi/fa/detail/BV15V41117u2
 * [Flut] 请求头:{auth-token: ZmEtMjAyMS0wNC0xMiAyMToyMjoyMC1mYQ==fa, course-flag: fa, boarding-pass: 5CECC91B29CD0BC0447210DCD918A1D4AF}
 * [Flut] 请求参:{}
 */
interface VideoDetailApi : com.jesen.retrofit_lib.BaseApi {

    @Headers(
        "$AUTH_TOKEN_K: $AUTH_TOKEN_V",
        "$COURSE_FLAG_K: $COURSE_FLAG_V"
    )
    @GET("/uapi/fa/detail/{videoId}")
    suspend fun requestVideoDetail(
        @Path("videoId") videoId: String,
    ): VideoDetailM

    override fun needLogin(): Boolean = true
}