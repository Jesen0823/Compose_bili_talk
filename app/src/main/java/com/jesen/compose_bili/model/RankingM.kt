package com.jesen.compose_bili.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RankingM(
    @Json(name = "code")
    val code: Int,
    @Json(name = "msg")
    val msg: String,
    @Json(name = "data")
    val data: RankingData?
)

@JsonClass(generateAdapter = true)
data class RankingData(
    @Json(name = "total")
    val total: Int,
    @Json(name = "list")
    val list: List<VideoM>?
)