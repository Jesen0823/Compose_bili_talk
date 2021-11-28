package com.jesen.compose_bili.network

object UserRepository {

    suspend fun startLogin(username: String, password: String) =
        RetrofitClient.createApi(LoginServiceApi::class.java)
            .requestLogin(username, password)
}