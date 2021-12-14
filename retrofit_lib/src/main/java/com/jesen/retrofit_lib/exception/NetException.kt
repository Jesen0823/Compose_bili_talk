package com.jesen.compose_bili.network.exception

/**
 * 网络异常统一格式
 * */
sealed class HiNetError(val code:Int, override val message:String, val data:String):Exception(message) {

    // 需要登录的异常
    class NeedLoginError(code:Int= 403,  message: String="请先登录",data: String="") : HiNetError(code,message,data)

    // 需要授权的异常
    class NeedAuthError(code:Int = 403, message:String,data:String) : HiNetError(code,message,data)

    fun copy( code:Int, message:String,  data:String): HiNetError {
        return when (this) {
            is NeedLoginError -> NeedLoginError(code, message, data)
            is NeedAuthError -> NeedLoginError(code, message, data)
        }
    }

}