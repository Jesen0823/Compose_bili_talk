package com.jesen.retrofit_lib.api

import com.jesen.retrofit_lib.model.TranslationM
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 搜索接口
 * 第三方接口：有道云翻译【与视频无关，仅仅为了效果展示】
 *
 * */
interface SearchTransApi {
    @GET("http://dict.youdao.com/suggest")
    suspend fun translateInput(
        @Query("q") key: String,
        @Query("num") num: Int = 10,
        @Query("doctype") docType: String = "json"
    ): TranslationM
}