package com.jesen.retrofit_lib.api

import com.jesen.retrofit_lib.com.AUTH_TOKEN_K
import com.jesen.retrofit_lib.com.AUTH_TOKEN_V
import com.jesen.retrofit_lib.com.COURSE_FLAG_K
import com.jesen.retrofit_lib.com.COURSE_FLAG_V
import com.jesen.retrofit_lib.model.NoticeListM
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * 通知列表
 *
 * [Flut] 请求url:https://api.devio.org/uapi/notice?pageIndex=1&pageSize=10
 * [Flut] 请求头:{auth-token: ZmEtMjAyMS0wNC0xMiAyMToyMjoyMC1mYQ==fa, course-flag: fa, boarding-pass: E793ED7A61088AAA70DD32614448F2C4AF}
 * [Flut] 请求参:{pageIndex: 1, pageSize: 10}
 */
interface NoticeListApi {

    @Headers(
        "$AUTH_TOKEN_K: $AUTH_TOKEN_V",
        "$COURSE_FLAG_K: $COURSE_FLAG_V"
    )
    @GET("uapi/notice")
    suspend fun requestNotice(
        @Query("pageIndex") pageIndex: Int = 0,
        @Query("pageSize") pageSize: Int = 10
    ): NoticeListM
}