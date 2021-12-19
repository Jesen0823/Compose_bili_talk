package com.jesen.compose_bili.repository

import com.jesen.retrofit_lib.RetrofitClient
import com.jesen.retrofit_lib.api.SearchTransApi

object SearchRepository {

    suspend fun searchTranslation(keyword: String) =
        RetrofitClient.createApi(SearchTransApi::class.java)
            .translateInput(key = keyword)
}