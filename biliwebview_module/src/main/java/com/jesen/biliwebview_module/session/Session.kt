package com.jesen.biliwebview_module.session

import okhttp3.Cookie

data class Session(
    var key: String,
    var value: String
) {

    fun toCookie() = Cookie.Builder()
        .name(key)
        .value(value)
        .domain("api.devio.org")
        .build()

    fun isNotEmpty() = key.isNotEmpty() && value.isNotEmpty()

    override fun toString(): String {
        return "$key=$value"
    }
}