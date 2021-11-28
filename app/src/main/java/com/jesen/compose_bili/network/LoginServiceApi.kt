package com.jesen.compose_bili.network

import com.jesen.compose_bili.model.UserResult
import com.jesen.compose_bili.utils.AUTH_TOKEN_K
import com.jesen.compose_bili.utils.AUTH_TOKEN_V
import com.jesen.compose_bili.utils.COURSE_FLAG_K
import com.jesen.compose_bili.utils.COURSE_FLAG_V
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * 登录API
 * [Flut] 请求url:https://api.devio.org/uapi/user/login?userName=hhj&password=12334&imoocId=null&orderId=null
 * [Flut] 请求头:{auth-token: ZmEtMjAyMS0wNC0xMiAyMToyMjoyMC1mYQ==fa, course-flag: fa}
 * [Flut] 请求参:{userName: hhj, password: 12334, imoocId: null, orderId: null}
 *
 * response: {"code":5004,"msg":"请先购买课程@https://coding.imooc.com/class/487.html"}
 *
 * */
interface LoginServiceApi : BaseApi {

    @Headers(
        "$AUTH_TOKEN_K: $AUTH_TOKEN_V",
        "$COURSE_FLAG_K: $COURSE_FLAG_V"
    )
    @POST("/uapi/user/login")
    suspend fun requestLogin(
        @Query("userName") userName: String,
        @Query("password") password: String,
        @Query("imoocId") imoocId: Int? = null,
        @Query("orderId") orderId: Int? = null,
    ): UserResult

    override fun needLogin(): Boolean = false
}
