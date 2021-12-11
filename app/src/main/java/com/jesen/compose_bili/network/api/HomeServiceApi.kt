package com.jesen.compose_bili.network.api

import com.jesen.compose_bili.model.HomeM
import com.jesen.compose_bili.network.BaseApi
import com.jesen.compose_bili.utils.AUTH_TOKEN_K
import com.jesen.compose_bili.utils.AUTH_TOKEN_V
import com.jesen.compose_bili.utils.COURSE_FLAG_K
import com.jesen.compose_bili.utils.COURSE_FLAG_V
import retrofit2.http.*

/**
 * 首页各栏目请求
 * [Flut] 请求url:https://api.devio.org/uapi/fa/home/%E6%8E%A8%E8%8D%90?pageIndex=1&pageSize=10
 * [Flut] 请求头:{auth-token: ZmEtMjAyMS0wNC0xMiAyMToyMjoyMC1mYQ==fa, course-flag: fa, boarding-pass: E793ED7A61088AAA70DD32614448F2C4AF}
 * [Flut] 请求参:{pageIndex: 1, pageSize: 10}
 */

interface HomeServiceApi : BaseApi {

    @Headers(
        "$AUTH_TOKEN_K: $AUTH_TOKEN_V",
        "$COURSE_FLAG_K: $COURSE_FLAG_V"
    )
    @GET("/uapi/fa/home/{categoryName}")
    suspend fun requestHomeCategory(
        @Path("categoryName") categoryName: String,
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int,
    ): HomeM

    override fun needLogin(): Boolean = true
}