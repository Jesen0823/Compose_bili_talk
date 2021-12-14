package com.jesen.compose_bili.repository

import com.jesen.retrofit_lib.api.LoginServiceApi
import com.jesen.retrofit_lib.api.RegisterServiceApi

/**
 * 用户登录注册
 * */
object UserRepository {

    suspend fun startLogin(username: String, password: String) =
        com.jesen.retrofit_lib.RetrofitClient.createApi(LoginServiceApi::class.java)
            .requestLogin(username, password)


    suspend fun startRegister(username: String, password: String,imoocId:Int,orderId:Int) =
        com.jesen.retrofit_lib.RetrofitClient.createApi(RegisterServiceApi::class.java)
            .requestRegister(username, password,imoocId,orderId)
}


