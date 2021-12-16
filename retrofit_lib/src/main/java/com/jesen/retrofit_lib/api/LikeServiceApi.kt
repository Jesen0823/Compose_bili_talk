package com.jesen.retrofit_lib.api

import com.jesen.retrofit_lib.BaseApi
import com.jesen.retrofit_lib.com.AUTH_TOKEN_K
import com.jesen.retrofit_lib.com.AUTH_TOKEN_V
import com.jesen.retrofit_lib.com.COURSE_FLAG_K
import com.jesen.retrofit_lib.com.COURSE_FLAG_V
import com.jesen.retrofit_lib.model.InteractionM
import retrofit2.http.*

/**
 * 点赞取消点赞请求
 * [Flut] url:https://api.devio.org/uapi/fa/like/BV1pV411z7sV
 * [Flut] 请求url:https://api.devio.org/uapi/fa/like/BV1pV411z7sV
 * [Flut] 请求头:{auth-token: ZmEtMjAyMS0wNC0xMiAyMToyMjoyMC1mYQ==fa, course-flag: fa, boarding-pass: 5CECC91B29CD0BC0447210DCD918A1D4AF, content-type: null}
 */

interface LikeServiceApi : BaseApi {

    /**
     * 发起点赞
     */
    @Headers(
        "$AUTH_TOKEN_K: $AUTH_TOKEN_V",
        "$COURSE_FLAG_K: $COURSE_FLAG_V"
    )
    @POST("/uapi/fa/like/{videoId}")
    suspend fun requestLikeVideo(
        @Path("videoId") videoId: String,
    ): InteractionM


    /**
     * 取消点赞
     */
    @Headers(
        "$AUTH_TOKEN_K: $AUTH_TOKEN_V",
        "$COURSE_FLAG_K: $COURSE_FLAG_V"
    )
    @DELETE("/uapi/fa/like/{videoId}")
    suspend fun requestCancelLikeVideo(
        @Path("videoId") videoId: String,
    ): InteractionM

    override fun needLogin(): Boolean = true
}