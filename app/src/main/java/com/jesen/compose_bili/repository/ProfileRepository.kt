package com.jesen.compose_bili.repository

import com.jesen.retrofit_lib.RetrofitClient
import com.jesen.retrofit_lib.api.ProfileInfoApi

object ProfileRepository {

    suspend fun getProfileData() =
        RetrofitClient.createApi(ProfileInfoApi::class.java)
            .requestProfileData()
}