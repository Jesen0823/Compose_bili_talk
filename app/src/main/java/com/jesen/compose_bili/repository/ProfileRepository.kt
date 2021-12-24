package com.jesen.compose_bili.repository

import com.jesen.common_util_lib.datastore.DataStoreUtil
import com.jesen.retrofit_lib.RetrofitClient
import com.jesen.retrofit_lib.api.ProfileInfoApi
import com.jesen.retrofit_lib.com.BOARDING_PASS

object ProfileRepository {

    // 获取个人中心页面数据
    suspend fun getProfileData() =
        RetrofitClient.createApi(ProfileInfoApi::class.java)
            .requestProfileData()

    // 退出登录
    suspend fun logoutAccount(){
            DataStoreUtil.saveStringData(BOARDING_PASS, "")
    }
}