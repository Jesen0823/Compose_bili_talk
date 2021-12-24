package com.jesen.retrofit_lib.model

/**
 * 登录注册结果
 *
 * */
data class UserResult(val code: Int, val msg: String?, val extra: String?, var data: String?)
