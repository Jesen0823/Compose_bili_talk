package com.jesen.retrofit_lib.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FavoriteListM(
    @Json(name = "code")
    val code: Int,
    @Json(name = "msg")
    val msg: String,
    @Json(name = "data")
    val data: FavoriteData?
)

@JsonClass(generateAdapter = true)
data class FavoriteData(
    @Json(name = "total")
    val total: Int,
    @Json(name = "list")
    val list: List<VideoM>?
)