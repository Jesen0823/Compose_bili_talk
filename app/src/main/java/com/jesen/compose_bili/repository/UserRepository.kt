package com.jesen.compose_bili.repository

import com.jesen.compose_bili.network.LoginServiceApi
import com.jesen.compose_bili.network.RegisterServiceApi
import com.jesen.compose_bili.network.RetrofitClient

/**
 * 用户登录注册
 * */
object UserRepository {

    suspend fun startLogin(username: String, password: String) =
        RetrofitClient.createApi(LoginServiceApi::class.java)
            .requestLogin(username, password)


    suspend fun startRegister(username: String, password: String,imoocId:Int,orderId:Int) =
        RetrofitClient.createApi(RegisterServiceApi::class.java)
            .requestRegister(username, password,imoocId,orderId)
}


